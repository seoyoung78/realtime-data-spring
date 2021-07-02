<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.0.1/mqttws31.min.js"></script>

<div>
	<div>MQTT</div>
	<hr/>
	<div>
		<button id="btnConnectWebSocket" class="btn btn-info btn-sm" 
				onclick="connectMqtt()">MQTT 브로커 접속하기</button>
		<button id="btnDisConnectWebSocket" class="btn btn-info btn-sm" 
				onclick="disconnectMqtt()"
				style="display:none;">MQTT 브로커 접속끊기</button>
		<script>
			$(function() {
				connectMqtt();
			});
		
	    	function connectMqtt() {
	            client = new Paho.MQTT.Client(window.location.hostname, 61614, 
	            							  "client-" + new Date().getTime());
	          	client.onConnectionLost = () => {
	          		console.log("접속 끊김");
		    		$("#btnConnectWebSocket").show();
	 				$("#btnDisConnectWebSocket").hide();
	          	};
	            client.onMessageArrived = (msg) => {
	            	console.log("메시지 수신");
	                var message = JSON.parse(msg.payloadString);
	                $("#contentView").append("<div>" + message.topic + ": " + 
	                						 message.content + "</div>");
	            };
	            client.connect({onSuccess: () => {
	            	console.log("접속 성공");
		    		$("#btnConnectWebSocket").hide();
	 				$("#btnDisConnectWebSocket").show();
	 				sendSubTopic();
	            }});
	 		}
	    	
	    	function disconnectMqtt() {
	    		client.disconnect();
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
				// 이전 구독 내용을 지우고 새로운 구독 내용 추가
				if(typeof(subscribeTopic) !== "undefined") {
	    			client.unsubscribe(subscribeTopic);
				}
				subscribeTopic = $("#subTopic").val();
 				client.subscribe(subscribeTopic);
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
				var pubTopic = $("#pubTopic").val();
				var content = $("#content").val();
				
				//서버 -> MQTT 브로커로 메시지 발신
				/* $.ajax({
					url: "sendMqttMessage",
					data: {topic:pubTopic, content},
					success: function(data) {
						//console.log(data);
					}
				}); */ 
				
				//직접 MQTT 브로커로 메시지 발신
				var message = {topic:pubTopic, content};
				message = JSON.stringify(message);
				client.send(pubTopic, message, 0, false);
			}  	
		</script>
	</div>
	
	<div class="mt-3" >
		<div>[수신 메시지]</div>
		<div id="contentView" class="mt-2 p-2" style="border:1px solid black;"></div>
	</div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
								

