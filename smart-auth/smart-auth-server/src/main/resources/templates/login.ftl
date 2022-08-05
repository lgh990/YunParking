<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<title>AG-Admin</title>
<link rel="stylesheet" type="text/css" href="./static/css/styles.css"/>
</head>
<body>
<div class="htmleaf-container">
	<div class="wrapper">
		<div class="container">
			<h1>AG-Enterprise 单点登录</h1>
			<form class="form" action="login" method="post">
				<input type="hidden" name="grant_type" value="password"/>
				<input type="text" name="username" autofocus placeholder="Username">
				<input type="password" name="password" placeholder="Password">
				<input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<button type="submit" id="login-button">登录</button>
			</form>
		</div>
		
		<ul class="bg-bubbles">
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
		</ul>
	</div>
</div>

<script src="./static/js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script>
$('#login-button').click(function (event) {
	$('form').fadeOut(200);
	$('.wrapper').addClass('form-success');
});
</script>
</body>
</html>