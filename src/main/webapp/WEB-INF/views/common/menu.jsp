<%@ page contentType="text/html; charset=UTF-8"%>

<ul class="nav flex-column">
  <li class="nav-item mb-2">
    <h6 class="text-white">Controller</h6>
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
</ul>












