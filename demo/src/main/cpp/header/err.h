//
// Created by zhangzhiqiang on 2019/10/18.
//

#ifndef STUDYONE_ERR_H
#define STUDYONE_ERR_H
#ifdef __cplusplus
extern "C" {
#endif
void errx(int, const char *, const char *);
void err(int, const char *,...);
#ifdef __cplusplus
}
#endif
#endif //STUDYONE_ERR_H
