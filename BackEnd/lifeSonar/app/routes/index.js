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


// console.log('test');


var dbConnectionPool=null;

/* GET home page. */
router.get('/test', function(req, res, next) {
    res.send({success:true, msg: 'alive'});
});

router.get('/getUserData', function(req, res, next) {
    let userId = req.query.userId;

    let mySqlConnUtils = getMySqlDbConnection();

    return async.waterfall([
        function(wfCallback){
            var sql = "SELECT heartRate, weight, height from userData where userId=" + userId;
            return mySqlConnUtils.query(sql,[],false,wfCallback);
        }
    ],function(error,results){

        if(error){
            return res.status(200).send({success: true, results: []});
        }

        return res.status(200).send({success: true, results: results});

    });

});

router.get('/getUser', function(req, res, next) {
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


function getMySqlDbConnection(){

    //console.log(config.mysqlsonfig)
    if(dbConnectionPool==null){
        dbConnectionPool = new mySqlConnectionUtils(config.mysqlsonfig);
    }
    return dbConnectionPool;

}




function getMySqlDbConnection(){

    //console.log(config.mysqlsonfig)
    if(dbConnectionPool==null){
        dbConnectionPool = new mySqlConnectionUtils(config.mysqlsonfig);
    }
    return dbConnectionPool;

}



module.exports = router;
