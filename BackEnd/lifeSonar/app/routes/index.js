var config = require("../../config/config.main");
var express = require('express');
var router = express.Router();
var async = require('async');
var utils = require('../../utils/generalUtils');
var uuid = require('node-uuid');
var formData = require('formidable');
var fs = require('fs');


var mySqlConnectionUtils = require('../../utils/mySqlUtils');
const mySqlConnUtils = getMySqlDbConnection();

var dbConnectionPool=null;

/* GET home page. */
router.get('/test', function(req, res, next) {
    res.send({success:true, msg: 'alive'});
});

router.get('/getLastUserData', function(req, res, next) {
    "use strict";

    let userId = req.query.userId;

    let mySqlConnUtils = getMySqlDbConnection();

    return async.waterfall([
        function(wfCallback){
            var sql = "SELECT temp, heartRate, weight, height, galvanic, created from userData where userId=" + userId + " order by created desc limit 1";
            return mySqlConnUtils.query(sql,[],false,wfCallback);
        }
    ],function(error,results){

        if(error){
            return res.status(200).send({success: true, results: []});
        }

        let weight = results[0].weight;
        let height = results[0].height;
        results[0].bmi = (weight/height/height) * 10000;

        return res.status(200).send({success: true, results: results});

    });

});

router.get('/getUser', function(req, res, next) {
    "use strict";

    let userId = req.query.userId;

    let mySqlConnUtils = getMySqlDbConnection();

    return async.waterfall([
        function(wfCallback){
            var sql = "SELECT `users`.`id`,\n" +
                "    `users`.`name`,\n" +
                "    `users`.`surname`,\n" +
                "    `users`.`username`,\n" +
                "    `users`.`password`,\n" +
                "    `users`.`created`,\n" +
                "    `users`.`updated`,\n" +
                "    `users`.`dob`,\n" +
                "    `users`.`age`\n" +
                "FROM `lifesonar`.`users` where id =" + userId;
            return mySqlConnUtils.query(sql,[],false,wfCallback);
        }
    ],function(error,results){

        if(error){
            return res.status(200).send({success: true, results: []});
        }

        return res.status(200).send({success: true, results: results});

    });

});

router.get('/getLastTemp', function(req, res, next) {
    "use strict";

    let userId = req.query.userId;

    let mySqlConnUtils = getMySqlDbConnection();

    return async.waterfall([
        function(wfCallback){
            var sql = "SELECT temp from userData where userId=" + userId + " order by created desc limit 1";
            return mySqlConnUtils.query(sql,[],false,wfCallback);
        }
    ],function(error,results){

        if(error){
            return res.status(200).send({success: true, results: []});
        }

        return res.status(200).send({success: true, results: results});

    });

});

router.get('/getAllData', function(req, res, next) {
    "use strict";

    let userId = req.query.userId;

    let mySqlConnUtils = getMySqlDbConnection();

    return async.waterfall([
        function(wfCallback){
            var sql = "SELECT \n" +
                "    `b`.`name`,\n" +
                "    `b`.`surname`,\n" +
                "    `a`.`heartRate`,\n" +
                "    `a`.`weight`,\n" +
                "    `a`.`height`,\n" +
                "    `a`.`created`,\n" +
                "    `a`.`updated`,\n" +
                "    `a`.`temp`,\n" +
                "    `a`.`galvanic`,\n" +
                "\t`a`.`id`,\n" +
                "    `a`.`userId`,\n" +
                "    `b`.`dob`\n" +

                "    \n" +
                "FROM\n" +
                "    lifesonar.userData a \n" +
                "    join lifesonar.users b on a.userId=b.id\n" +
                "GROUP BY a.userId\n" +
                "ORDER BY b.created;\n" +
                "\n" +
                "\n";
            return mySqlConnUtils.query(sql,[],false,wfCallback);
        }
    ],function(error,results){

        if(error){
            return res.status(200).send({success: true, results: []});
        }

        for (var i = 0, len = results.length; i < len; i++) {
            let weight = results[i].weight;
            let height = results[i].height;

            results[i].bmi = ((weight/height/height) * 10000).toFixed(2);
        }



        return res.status(200).send({success: true, results: results});

    });

});
function getMySqlDbConnection(){

    //console.log(config.mysqlsonfig)
    if(dbConnectionPool==null){
        dbConnectionPool = new mySqlConnectionUtils(config.mysqlsonfig);
    }
    return dbConnectionPool;

}

module.exports = router;
