export default class Constants {
    /**
     * 登录后键名
     * **/
    static LOGIN_PARMES = {
        USER_NAME: "userName",
        USER_TOKEN: "token"
    };

    /**
     * 公用部分
     * **/
    static PUBLICCOMMON_ALL = {
        code: 0,
        codeName: "全部"
    };

    /**
     * 状态
     * @parme “正常”表示可以自动获取股票数据了 “待转正”表示当前记录还不完善。
     * **/
    static PUBLICCOMMON_STATUS = [
        { code: 1, codeName: "上线" },
        { code: 2, codeName: "下线" }
    ];

    /**
     * 当前日期状态
     * **/
    static PUBLICCOMMON_FESTIVAL = [
        { code: 1, codeName: "节假日" },
        { code: 2, codeName: "自定义" }
    ];
    /***
     * 股票全局设置
     * **/
    static FINANCE = {
        /**
         * 指数类型
         * **/
        INDEX_TYPE: [
            { code: 1, codeName: "沪指" },
            { code: 2, codeName: "深指" }
        ],
        /**
         * 指数类型
         * **/
        INDEX_TYPE_CODE: [
            { code: 1, codeName: ".SH" },
            { code: 2, codeName: ".SZ" }
        ],
        /**
         * 数据注入类型
         * @parme “正常”表示可以自动获取股票数据了 “待转正”表示当前记录还不完善。
         * **/
        ADDSOURCE: [
            { code: 1, codeName: "定时器" },
            { code: 2, codeName: "导入" },
            { code: 3, codeName: "手动添加" }
        ],
        /**
         * 股票更新状态---股票更新表用到
         * @parme 1成功 2失败
         * **/
        UPDATE_STATUS: [
            { code: 1, codeName: "成功" },
            { code: 2, codeName: "失败" }
        ]
    };
    /***
     * 考题类型
     * **/
    static GRDEQUESTION = {
        /**
         *考题--数据类型状态
         * @parme
         * **/
        KAOTI_TYPE: [
            { code: 1, codeName: "试卷种类" }, //例如，语文，数学，英语等等
            { code: 2, codeName: "所属年级" }, //例如，一年级，大一等
            { code: 3, codeName: "考题来源" }, //例如，苏教版，某年考题等
            { code: 4, codeName: "考题类型" } //例如，应用题，字词积累等
        ],

        /**
         * 考题类型
         * **/
        KAOTI_QUESTION_TYPE: [
            { code: 1, codeName: "看拼音写汉字" },
            { code: 2, codeName: "看汉字写拼音" }
        ]
    };
}
