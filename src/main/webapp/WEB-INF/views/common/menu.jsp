<%@ page contentType="text/html; charset=UTF-8"%>

<ul class="nav flex-column">
  <li class="nav-item mb-2">
    <h6 class="text-white">제공 소스</h6>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>">
    	Home
    </a>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>/chat">
    	WebSocket
    </a>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>/redis">
    	WebSocket + Redis
    </a>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>/mqtt">
    	MQTT
    </a>
  </li>
  
  <li class="nav-item mb-2">
    <h6 class="text-white">연습 소스</h6>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>/chat2">
    	WebSocket
    </a>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>/redis2">
    	WebSocket + Redis
    </a>
    <a class="nav-link text-warning" href="<%=application.getContextPath()%>/mqtt2">
    	MQTT
    </a>
  </li>
</ul>












