package com.example.demo.anim.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.anim.presenter.AttrAnimPresenter;
import com.example.demo.anim.view.IAttrAnimView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class AttrAnimActivity extends BaseActivity implements IAttrAnimView {
    @BindView(R2.id.one)
    ImageView one;
    @BindView(R2.id.two)
    ImageView two;
    @BindView(R2.id.content)
    ScrollView contentView;
    @BindView(R2.id.loading_spinner)
    ProgressBar loadingView;
    @BindView(R2.id.circularReveal)
    ImageView myView;
    @BindView(R2.id.cir)
    View cir;
    @BindView(R2.id.thumb_button_1)
    ImageButton thumb1View;
    @BindView(R2.id.expanded_image)
    ImageView expandedImage;
    @BindView(R2.id.linear_container)
    LinearLayout linearContainer;
    private AttrAnimPresenter mPresenter;
    AnimatedVectorDrawable drawable;
    long shortAnimationDuration;
    private boolean showingBack, isOpen;
    private AnimatorSet currentAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attr_anim;
    }

    @Override
    public void bindData() {
        setTitle("属性动画");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = (AnimatedVectorDrawable) two.getDrawable();
        }
        // Initially hide the content view.
        contentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new AttrAnimPresenter();
        }
        return mPresenter;
    }

    private void startOneAnim() {
        // 步骤1：设置需要组合的动画效果
        ObjectAnimator translation = ObjectAnimator.ofFloat(one, "translationX", 0, 300, 0);
        // 平移动画
        ObjectAnimator rotate = ObjectAnimator.ofFloat(one, "rotation", 0f, 360f);
        // 旋转动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(one, "alpha", 1f, 0f, 1f);
        // 透明度动画

        // 步骤2：创建组合动画的对象
        AnimatorSet animSet = new AnimatorSet();

        // 步骤3：根据需求组合动画
        animSet.play(translation).with(rotate).before(alpha);
        //会为每一个动画设置10秒
        animSet.setDuration(10000);

        // 步骤4：启动动画
        animSet.start();
    }

    @OnClick({R2.id.one, R2.id.two, R2.id.thumb_button_1})
    public void onClick(View view) {
        if (view.getId() == R.id.one) {
            //startOneAnim();
            //enterOne();
            //flipCard();
            //circularReveal();
            //path();
            dynamic();
        } else if (view.getId() == R.id.two) {
            if (drawable != null) {
                drawable.start();
            }
        } else if (view.getId() == R.id.thumb_button_1) {
            //zoomImageFromThumb(thumb1View, R.drawable.a_0);
            //bound();
//            ImageView imageView = new ImageView(this);
//            imageView.setImageResource(R.drawable.a_1);
//            linearContainer.addView(imageView);
            sceneV2();
        }
    }

    //放大缩小动画
    private void zoomImageFromThumb(ImageButton thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container2)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        //转换为本地坐标系
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(3000);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(3000);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    //创建淡入淡出动画
    private void enterOne() {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    //为卡片翻转添加动画
    private void flipCard() {
        if (showingBack) {
            getSupportFragmentManager().popBackStack();
            showingBack = false;
            return;
        }

        // Flip to the back.

        showingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        getSupportFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.container, new CardBackFragment())

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
    }

    //创建圆形揭露动画
    private void circularReveal() {
        if (isOpen) {
            // Check if the runtime version is at least Lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // get the center for the clipping circle
                int cx = myView.getWidth() / 2;
                int cy = myView.getHeight() / 2;

                // get the initial radius for the clipping circle
                float initialRadius = (float) Math.hypot(cx, cy);

                // create the animation (the final radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0f);

                // make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        myView.setVisibility(View.INVISIBLE);
                    }
                });

                // start the animation
                anim.start();
            } else {
                // set the view to visible without a circular reveal animation below Lollipop
                myView.setVisibility(View.VISIBLE);
            }
        } else {
            // Check if the runtime version is at least Lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // get the center for the clipping circle
                int cx = myView.getWidth() / 2;
                int cy = myView.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius);

                // make the view visible and start the animation
                myView.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                // set the view to invisible without a circular reveal animation below Lollipop
                myView.setVisibility(View.INVISIBLE);
            }
        }
        isOpen = !isOpen;
    }

    //path动画
    private void path() {
        // arcTo() and PathInterpolator only available on API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Path path = new Path();
//            path.lineTo(1,1);
//            //path.arcTo(0f, 0f, 1.0f, 1.0f, 270f, -180f, true);
//            PathInterpolator pathInterpolator = new PathInterpolator(path);
//            ObjectAnimator animation = ObjectAnimator.ofFloat(cir, "translationX", 100f);
//            animation.setInterpolator(pathInterpolator);
//            animation.start();
            Path path = new Path();
            path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(cir, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.start();

            Interpolator interpolator = AnimationUtils.loadInterpolator(this, R.anim.path_interpolator);
        }
    }

    //创建投掷动画
    private void dynamic() {
        FlingAnimation fling = new FlingAnimation(cir, DynamicAnimation.TRANSLATION_X);
        fling.setStartVelocity(1000)
                .setMinValue(0)
                .setMaxValue(200)
                .setFriction(1.1f)
                .start();
    }

    //创建弹簧动画
    private void bound() {
        final View img = thumb1View;
        // Setting up a spring animation to animate the view’s translationY property with the final
        // spring position at 0.
        final SpringAnimation springAnim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 200);
        SpringForce force = springAnim.getSpring();
        force.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW);
        springAnim.setSpring(force);
        //springAnim.start();
        springAnim.animateToFinalPosition(400);
    }

    //创建场景动画
    private void scene(){
        Scene aScene;
        Scene anotherScene;

        // Create the scene root for the scenes in this app
        ViewGroup sceneRoot = findViewById(R.id.scene_root);

        // Create the scenes
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.a_scene, this);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.b_scene, this);
        //new创建场景
        // Obtain the view hierarchy to add as a child of
        // the scene root when this scene is entered
        View viewHierarchy = (ViewGroup) findViewById(R.id.scene_container);
        // Create a scene
        Scene mScene = new Scene(sceneRoot, viewHierarchy);

//        Transition fadeTransition =
//                TransitionInflater.from(this).
//                        inflateTransition(R.transition.fade_tran);
        Transition fadeTransition = new Fade();
        TransitionManager.go(anotherScene, new AutoTransition());
    }
    //没有场景的过渡
    private void sceneV2(){
        // Create a new TextView and set some View properties
        TextView labelText = new TextView(this);
        labelText.setText("Label");
        labelText.setId(R.id.text);

        // Get the root view and create a transition
        ViewGroup rootView = (ViewGroup) findViewById(R.id.mainLayout);
        Fade mFade = new Fade(Fade.IN);
        // Start recording changes to the view hierarchy
        TransitionManager.beginDelayedTransition(rootView, mFade);
        // Add the new TextView to the view hierarchy
        rootView.addView(labelText);
    }
    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AttrAnimActivity.class);
        context.startActivity(starter);
    }
}