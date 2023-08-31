export default class LangUtil {
    /**
     * 值是否为空
     *  - 若是 undefined 或 null 则表示空，包括字符串的 'undefined' 'null' 'none'
     * @param val
     * @returns {boolean}
     */
    static isNil(val) {
        return !!(typeof val === "undefined" || val === null || ~["undefined", "null"].indexOf(`${val}`.toLowerCase()));
    }

    /**
     * 加全部属性
     * arr数组
     * obj对象
     * **/
    static addAll(arr) {
        if (null == arr || arr.length === 0) {
            return arr;
        }
        let arr1 = [];
        arr.forEach(element => {
            arr1.push(element);
        });
        arr1.unshift({ code: 0, codeName: "全部" });
        return arr1;
    }

    /**
     * 数字金额转为中文
     * **/
    static numChanageChines(money) {
        //汉字的数字
        var cnNums = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");
        //基本单位
        var cnIntRadice = new Array("", "拾", "佰", "仟");
        //对应整数部分扩展单位
        var cnIntUnits = new Array("", "万", "亿", "兆");
        //对应小数部分单位
        var cnDecUnits = new Array("角", "分", "毫", "厘");
        //整数金额时后面跟的字符
        var cnInteger = "";
        //整型完以后的单位
        var cnIntLast = "股";
        //最大处理的数字
        var maxNum = 999999999999999.9999;
        //金额整数部分
        var integerNum;
        //金额小数部分
        var decimalNum;
        //输出的中文金额字符串
        var chineseStr = "";
        //分离金额后用的数组，预定义
        var parts;
        if (money == "") {
            return "";
        }
        money = parseFloat(money);
        if (money >= maxNum) {
            //超出最大处理数字
            return "";
        }
        if (money == 0) {
            chineseStr = cnNums[0] + cnIntLast + cnInteger;
            return chineseStr;
        }
        //转换为字符串
        money = money.toString();
        if (money.indexOf(".") == -1) {
            integerNum = money;
            decimalNum = "";
        } else {
            parts = money.split(".");
            integerNum = parts[0];
            decimalNum = parts[1].substr(0, 4);
        }
        //获取整型部分转换
        if (parseInt(integerNum, 10) > 0) {
            var zeroCount = 0;
            var IntLen = integerNum.length;
            for (var i = 0; i < IntLen; i++) {
                var n = integerNum.substr(i, 1);
                var p = IntLen - i - 1;
                var q = p / 4;
                var m = p % 4;
                if (n == "0") {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        chineseStr += cnNums[0];
                    }
                    //归零
                    zeroCount = 0;
                    chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
                }
                if (m == 0 && zeroCount < 4) {
                    chineseStr += cnIntUnits[q];
                }
            }
            chineseStr += cnIntLast;
        }
        //小数部分
        if (decimalNum != "") {
            var decLen = decimalNum.length;
            for (var i = 0; i < decLen; i++) {
                var n = decimalNum.substr(i, 1);
                if (n != "0") {
                    chineseStr += cnNums[Number(n)] + cnDecUnits[i];
                }
            }
        }
        if (chineseStr == "") {
            chineseStr += cnNums[0] + cnIntLast + cnInteger;
        } else if (decimalNum == "") {
            chineseStr += cnInteger;
        }
        return chineseStr;
    }

    //将阿拉伯数字转换为大写数字
    static numChanage(n) {
        let cnum = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'];
        let s = '';
        n = '' + n; // 数字转为字符串
        for (var i = 0; i < n.length; i++) {
            s += cnum[parseInt(n.charAt(i))];
        }
        if (s.length == 2) { // 两位数的时候
            // 如果个位数是0的时候，0改成十
            if (s.charAt(1) == cnum[0]) {
                s = s.charAt(0) + cnum[10];
                // 如果是一十改成十
                if (s == cnum[1] + cnum[10]) {
                    s = cnum[10]
                }
            } else if (s.charAt(0) == cnum[1]) {
                // 如果十位数是一的话改成十
                s = cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[2]) {
                // 如果十位数是二的话改成十二
                s = cnum[2] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[3]) {
                s = cnum[3] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[4]) {
                s = cnum[4] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[5]) {
                s = cnum[5] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[6]) {
                s = cnum[6] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[7]) {
                s = cnum[7] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[8]) {
                s = cnum[8] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[9]) {
                s = cnum[9] + cnum[10] + s.charAt(1);
            }else if (s.charAt(0) == cnum[10]) {
                s = cnum[10] + cnum[10] + s.charAt(1);
            }
        }
        return s;
    }

}
