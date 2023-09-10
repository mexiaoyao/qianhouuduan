/**
 * 表单验证
 */
// 验证手机号
export function phoneCheck(rule, value, callbackFn) {
    const reg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
    if (!reg.test(value)) {
        callbackFn("请输入手机号码");
        return;
    }
    callbackFn();
}
// 验证固定电话
export function gphoneCheck(rule, value, callbackFn) {
    const reg = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
    if (!reg.test(value)) {
        callbackFn("请输入正确的固定号码");
        return;
    }
    callbackFn();
}
// 验证用户名，保证以字母开头  ^[a-z]\w{4,19}$
export function userNameCheck(rule, value, callbackFn) {
    const reg = /^[a-z]\w{7,19}$/;
    if (!reg.test(value)) {
        callbackFn("设置用户名，以字母开头，8-20位");
        return;
    }
    callbackFn();
}
// 验证密码
export function pwdCheck(rule, value, callbackFn) {
    const reg = /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{6,20}$/;
    if (!reg.test(value)) {
        callbackFn("请输入密码，密码由6-20位字母、字符或数字组成");
        return;
    }
    callbackFn();
}
// 邮箱验证
export function emailCheck(rule, value, callbackFn) {
    const reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if (!reg.test(value)) {
        callbackFn("请输入正确的邮箱");
        return;
    }
    callbackFn();
}
// 股票号码验证
export function gpCodeCheck(rule, value, callbackFn) {
    const reg = /^\d{6}$/;
    if (!reg.test(value)) {
        callbackFn("请输入正确的股票号码");
        return;
    }
    callbackFn();
}
// 正整数校验
export function zhengshuCheck(rule, value, callbackFn) {
    const reg = /^[1-9]+[0-9]*$/;
    if (!reg.test(value)) {
        callbackFn("请输入大于0的整数");
        return;
    }
    callbackFn();
}

// 布尔校验
export function booleanCheck(rule, value, callbackFn) {
    console.log("value:" + value);
    if (!value) {
        callbackFn("请请确认");
        return;
    }
    callbackFn();
}
