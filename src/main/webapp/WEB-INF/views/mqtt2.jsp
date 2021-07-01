<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.0.1/mqttws31.min.js"></script>

<div>
	<div>MQTT</div>
	<hr/>
	<div>
		<button id="btnConnectMqtt" class="btn btn-info btn-sm" 
				onclick="connectMqtt()">MQTT 브로커 접속하기</button>
		<button id="btnDisConnectMqtt" class="btn btn-info btn-sm" 
				onclick="disconnectMqtt()"
				style="display:none;">MQTT 브로커 접속끊기</button>
		<script>
			$(function() {
				connectMqtt();
			});
		
    	function connectMqtt() {
    		// 생성과 동시에 연결
    		client = new Paho.MQTT.Client(window.location.hostname, 61614, "client-" + new Date().getTime());
    		
    		// 연결 끊길 시 실행
    		client.onConnectionLost = () => {
    			console.log("접속 끊김");
    			$("#btnConnectMqtt").show();
    			$("#btnDisConnectMqtt").hide();
    		};
    		
    		// 메시지가 도착했을 경우
    		client.onMessageArrived = (msg) => {
    			console.log("메시지 수신: ", msg);		//{"topic":"/topic1/#", "content":"xxx"}
    			var message = JSON.parse(msg.payloadString);
    			$("#contentView").append("<div>" + message.topic + ": " + message.content + "</div>")
    		};
    		
    		client.connect({onSuccess: () => {
    			console.log("접속 성공");
    			$("#btnConnectMqtt").hide();
    			$("#btnDisConnectMqtt").show();
    			// 연결 시 수신 자동 설정
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
				// subscribe의 누적 방지
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
				var topic = $("#pubTopic").val();
				var content = $("#content").val();
				
				$.ajax({
					url: "sendMqttMessage2",
					data: {topic, content}
				}).done((data) => {
					// data: {result: "success"}
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
								

