console.log("Reply MOdule...........");

let replyService = (function(){

    //add start
    function add(reply,callback,error) {
        console.log("reply.........");

        $.ajax({
            type: "post",
            url:'/replies/new',
            data: JSON.stringify(reply),    //js -> josn
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                if(callback){
                    callback(result);
                }
            },
            error : function(xhr,status,er){
                if(error){
                    error(er);
                }
            }
        })
    }   //add end

    //getList start
    function getList(param,callback,error){
        console.log("js getList.....")
        let bno = param.bno;
        let page = param.page||1;

        $.ajax({
            type:'get',
            url:"/replies/pages/" + bno + "/" + page,
            //success : function(result,status,xhr) 였는데
            
            success : function(data,status,xhr){
                if(callback){
                  //callback(data) 	//댓글목록만 가져오는 경우
                  callback(data);		//댓글 숫자와 목록을 가져오는 경우
                }
            },
            error : function(xhr,status,er){
                if(error){
                    error(er);
                }
            }
        });
    }
    //getList end

    //remove start
    function remove(rno,callback,error){
        $.ajax({
            type:'delete',
            url:'/replies/'+ rno,
            success: function(deleteResult,status,xhr){
                if(callback){
                    callback(deleteResult);
                }
            },
            error: function(xhr,status,er){
                if(error){
                	error(er);
                }
            }
        })
    }
    //remove end
    
    //update start
    function update(reply,callback,error){
        $.ajax({
            type: 'put',
            url: '/replies/' + reply.rno,
            data: JSON.stringify(reply),
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                if(callback){
                    callback(result);
                }
            },
            error: function(xhr,status,er){
                if(error){
                    error(er);
                }
            }
        });
    }
    //update end

    //get start
    function get(rno,callback,error){
        $.ajax({
            type: 'get',
            url: '/replies/' + rno,
            success: function(replyVO,status,xhr){
                if(callback){
                    callback(replyVO);
                }
            },
            error: function(xhr,status,er){
                if(error){
                    error(er);
                }
            }
        });
    }
    //get end

    //displayTime start
    function displayTime(timeValue){
        let today = new Date();
        let gap = today.getTime() - timeValue;

        let dateObj = new Date(timeValue);
        let str="";

        if(gap <(1000*60*60*24)){
            let hh = dateObj.getHours();
            let mi = dateObj.getMinutes();
            let ss = dateObj.getSeconds();

            return [ (hh>9?'':'0') + hh, ':' , (mi>9? '' : '0') + mi , ':', (ss>9 ? '' : '0') + ss ].join('');
        }else{
            let yy = dateObj.getFullYear();
            let mm = dateObj.getMonth()+1;      //1월 :0,2월:1 =>1월:0+1
            let dd = dateObj.getDate();

            return [ yy ,'/', (mm>9? '':'0') + mm ,'/', (dd>9? '':'0') + dd].join('');
        }

    }
    //displayTime end


    return{
        add : add,
        getList : getList,
        remove : remove,
        update : update,
        get : get,
        displayTime : displayTime
    };
})();   