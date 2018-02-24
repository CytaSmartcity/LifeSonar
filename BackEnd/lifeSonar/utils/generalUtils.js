
module.exports = {

    isObjectEmpty: function ( param) {

        if(typeof param == 'object'){

            for (var key in param) {
                if(typeof param[key] == 'undefined' || param[key] == null || param[key]== ""){
                    return {isEmpty:true, inputName: key};
                }
            }
            return {isEmpty:false, inputName: null};

        }else if(typeof param == 'undefined' ) {

            return {isEmpty:true, undefined: true, inputName: key};

        }else{

            if(typeof param == 'undefined' || param == null || param== ""){
                return {isEmpty:true, inputName: null};
            }else{
                return {isEmpty:false, inputName: null};
            }

        }

    },

    isVariableEmpty: function (param) {
        if(typeof param == 'undefined' || param == null || param == ""){
            return true;
        }else if(typeof param == 'undefined' ) {
            return true;
        }else{
            return false;
        }

    }

};
