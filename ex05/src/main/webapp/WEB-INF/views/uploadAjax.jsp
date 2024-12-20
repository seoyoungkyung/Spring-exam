<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>


<style>
.uploadResult {
   width: 100%;
   background-color: gray;
}

.uploadResult ul {
   display: flex;
   flex-flow: row;
   justify-content: center;
   align-items: center;
}

.uploadResult ul li {
   list-style: none;
   padding: 10px;
}

.uploadResult ul li img {
   width: 100px;
   
}

.uploadResult ul li span{
	color:white;
}
</style>

<style>
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray; 
  z-index: 100;
}

.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}


.bigPicture img{
	width:600px;
}
</style>

</head>
<body>
	<h1>uploadAjax</h1>
	
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple="multiple">
	</div>
	
	
	<button id="uploadBtn">Upload</button>
	
	<div class="uploadResult">
		<ul>
		</ul>
	</div>
	
	
	<div class=bigPictureWrapper>
		<div class="bigPicture">
		</div>
	</div>
	
	
	<script type="text/javascript">
	
		function showImage(fileCallPath){
			//alert(fileCallPath);
			$(".bigPictureWrapper").css("display","flex").show();
			$(".bigPicture").html("<img src='/display?fileName=" +encodeURI(fileCallPath)+"'>").animate({width:'100%',height: '100%'},1000);
			
		}//end showImage
		
		$(".bigPictureWrapper").on("click",function(e){
			$(".bigPicture").animate({width:'0%' , height:'0%'},1000);
			setTimeout(() => {
				$(this).hide();
			},1000);
		})
	
		$(".uploadResult").on("click","span",function(e){
			let targetFile = $(this).data("file");
			let type = $(this).data("type");
			console.log(targetFile);
			
			$.ajax({
				url: '/deleteFile',
				data: {fileName:targetFile,type:type },
				dataType:'text',
				type:'post',
				success: function(result){
					alert(result);
				}
			});		//end $.ajax
		})
		
		$(document).ready(function(){
			
			let regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			
			let maxSize = 5242880;			//5MB
			
			function checkExtentsion(fileName, fileSize){
				if(fileSize >= maxSize){
					alert("파일 사이즈 초과");
					return false;
				} 	
				
				if(regex.test(fileName)){
					alert("해당 종류의 파일은 업로드 할 수 없습니다.");
					return false;
				}
				
				return true;
			}
			
			let cloneObj = $(".uploadDiv").clone();
			
			
			$("#uploadBtn").on("click",function(e){
				let formData = new FormData();
				
			let inputFile = $("input[name='uploadFile']");
			let files = inputFile[0].files;
			
			console.log(files);
			
			//add File Data to formData
			for(let i = 0; i <files.length; i++){
				
				if( !checkExtentsion(files[i].name, files[i].size)){
					return false;
				}
				formData.append("uploadFile",files[i]);				
			}
			
			$.ajax({
				url: '/uploadAjaxAction',
				processData: false,
				contentType: false,
				data: formData,
				type: 'post',
				dataType: 'json',
				success: function(result){
					
					console.log(result);
					showUploadFile(result);
					$(".uploadDiv").html(cloneObj.html());		//파일선택 옆 글씨 초기화
				}
			});			// $.ajax
		});			//end uploadBtn
		
		let uploadResult = $(".uploadResult ul");
		
		function showUploadFile(uploadResultArr){
			let str="";
			
			$(uploadResultArr).each(function(i,obj){
				
				if(!obj.image){
					
					let fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
					
					
					str += "<li><div><a href='/download?fileName="+fileCallPath+"'>"+"<img src='/resources/img/attach.png'>"
						+ obj.fileName + "</a>"+"<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"+"<div></li>";
			
				}else{
					//이미지 파일 인 경우
					//str += "<li>" + obj.fileName + "</li>";
					
					let fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
					
					let originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
					console.log("originPath before : " + originPath);
					
					originPath = originPath.replace(new RegExp(/\\/g),"/");
					console.log("originPath after : " + originPath);
					
					
					str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"+"<img src= '/display?fileName="+fileCallPath+"'></a>"+"<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span>"+"</li>";
				}
			});
			
			uploadResult.append(str);
		}	// end showUploadFile
		
		
		
		
		
	});	//end document
		
	</script>
</body>
</html>