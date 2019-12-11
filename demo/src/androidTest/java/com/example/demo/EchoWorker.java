package com.example.demo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class EchoWorker extends Worker {
    public EchoWorker(Context context, WorkerParameters parameters) {
        super(context, parameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data input = getInputData();
        if (input.size() == 0) {
            return Result.failure();
        } else {
            return Result.success(input);
        }
    }
}