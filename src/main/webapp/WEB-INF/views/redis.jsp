<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div>
	<div>WebSocket + Redis</div>
	<hr/>
	<div>
		<button id="btnConnectWebSocket" class="btn btn-info btn-sm" 
				onclick="connectWebSocket()">웹소켓 접속하기</button>
		<button id="btnDisConnectWebSocket" class="btn btn-info btn-sm" 
				onclick="disconnectWebSocket()"
				style="display:none;">웹소켓 접속끊기</button>
		<script>
			$(function() {
				connectWebSocket();
			});
		
	    	function connectWebSocket() {
	 			ws = new WebSocket("ws://" + window.location.host + "/websocket/redis");
	 			
	 			ws.onopen = function() {
	 				console.log("접속 성공");
	 				$("#btnConnectWebSocket").hide();
	 				$("#btnDisConnectWebSocket").show();
	 				sendSubTopic();
	 			};
	 			
	 			ws.onclose = function() {
	 				console.log("접속 끊김");
	    			$("#btnConnectWebSocket").show();
	 				$("#btnDisConnectWebSocket").hide();
	 			};
	 			
	 			ws.onmessage = function(event) {
	 				console.log("메시지 수신");
	 				var strJson = event.data;
	 				var message = JSON.parse(strJson);
	 				$("#contentView").append("<div>" + message.topic + ": " + message.content + "</div>");
	 			}
	 		}
	    	
	    	function disconnectWebSocket() {
    			ws.close();
	    	}
		</script>
	</div>
	
	<div class="mt-2" style="border:1px solid green; padding:5px;">
		<div>[수신 설정]</div> 
		<div>
			Topic: <input id="subTopic" type="text" value="/topic1/#"/>
			<button class="btn btn-info btn-sm" 
					onclick="sendSubTopic()">설정(접속시 자동 구독)</button>
		</div>
		<script>
			function sendSubTopic() {
	    		var subTopic = $("#subTopic").val();
	    		var json = {topic:subTopic};    		
	    		var strJson = JSON.stringify(json);
	    		ws.send(strJson);
	    	} 		
		</script>	
	</div>	
	
	<div class="mt-2" style="border:1px solid green; padding:5px;">
		<div>[발신]</div>
		<div>
			Topic: <input id="pubTopic" type="text" value="/topic1/topic2"/>
			Content: <input id="content" type="text" value="Hello"/>
			<button class="btn btn-info btn-sm" onclick="publishTopic()">보내기</button>
		</div>
		<script>
			function publishTopic() {
				var topic = $("#pubTopic").val();
				var content = $("#content").val();
				$.ajax({
					url: "sendRedisMessage",
					data: {topic, content},
					success: function(data) {
						//console.log(data);
					}
				});
			}  	
		</script>
	</div>
	
	<div class="mt-3" >
		<div>[수신 메시지]</div>
		<div id="contentView" class="mt-2 p-2" style="border:1px solid black;"></div>
	</div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
								

