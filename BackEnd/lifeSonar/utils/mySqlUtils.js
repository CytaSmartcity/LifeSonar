var mysql= require('mysql');

// Constructor
var mySqlConnectionUtils = function (poolParameters){
    this.pool = mysql.createPool(poolParameters);
}

// class methods
mySqlConnectionUtils.prototype.query = function query(sqlQuery, parameters, returnOnlyFirstRow, callback){

    this.pool.getConnection(function(err,connection){
        if (err) {
            console.log('Database error: ' + err.message)
            if(typeof connection != 'undefined'){
                connection.release();
            }
            return setImmediate(function(){callback(new Error(err.code),null)});
        }

        //console.log('connected as id ' + connection.threadId);


        connection.query(sqlQuery,parameters,function(err,rows){
            connection.release();
            if(!err) {
                if(returnOnlyFirstRow){
                    if(rows.length>0) {
                        var newRows = rows[0];
                        return callback(null, newRows);
                    }
                }
                //console.log(rows);
                return setImmediate(function(){callback(null,rows)});
            }else{
                console.log(err)
                return setImmediate(function(){callback(new Error(err.code),null)});
            }

        });

        connection.on('error', function(err) {
            //return callback(new Error(err.code),null);
            return setImmediate(function(){callback(new Error(err.code),null)});
        });
    });


}

//second way of creating a query
mySqlConnectionUtils.prototype.query2 = function query(sqlQuery, parameters, callback){

    this.pool.query(sqlQuery,parameters,function(err,rows){
        if (err) {
            connection.release();
            res.json({"code" : 100, "status" : "Error in connection database"});
            return callback(new Error(err.message),null);
        }
        // return callback(null,rows);
        return setImmediate(function(){callback(null,rows)});
        //console.log('connected as id ' + connection.threadId);

    });


}

module.exports = mySqlConnectionUtils;