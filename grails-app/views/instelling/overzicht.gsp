<html>
<head>
	<meta name="layout" content="beheer">
	<title><g:message code="settings" /></title>
	<style>
		#emailSettings{
			display:inline-block;
		}
		
		#emailSettings input{
			float:right;
			margin-bottom: 3pt;
		}
		
		#emailSettings h2{
			clear:both;
		}
		
		#emailSettings label{
			float:left;
			clear:both;
			line-height: 16px;
		}
		
		#emailSettings span
		{
			float:left;
			font-size: 12px;
			line-height: 16px;
		}
		
	</style>
</head>
<body>
	<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
	</g:if>
	<g:form action="submit">
		<label for="allowRequestsFrom"><g:message code="settings.allow_requests_from"/></label>
		<g:select name="allowRequestsFrom" from="${['localhost', 'local_network', 'everywhere']}"
		          valueMessagePrefix="settings.allow_requests_from" value="${allowRequests}"/><br/>
		<g:submitButton name="submitButton" value="${g.message(code:'save')}"/>
	</g:form>
	<br/>
	<h1><g:message code="settings.email"/></h1>
	<g:form action="submitEmail">
		<div id="emailSettings">
			<h2><g:message code="settings.email.smtp"/></h2>
			<label for="smtphost"><g:message code="settings.email.smtp.host"/></label><span class="example">bijv. smtp.gmail.com</span><g:textField name="smtphost" value="${smtphost}"/><br/>
			<label for="smtpport"><g:message code="settings.email.smtp.port"/></label><span class="example">bijv. 465</span><g:field type="number" name="smtpport" value="${smtpport}"/><br/>
			<label for="smtpusername"><g:message code="settings.email.smtp.username"/></label><g:textField name="smtpusername" value="${smtpusername}"/><br/>
			<label for="smtppassword"><g:message code="settings.email.smtp.password"/></label><g:passwordField name="smtppassword" value="${smtppassword}"/><br/>
			
			<br/>
			<h2><g:message code="settings.email.sender"/></h2>
					
			<label for="sendername"><g:message code="settings.email.sendername"/></label><g:textField style="float:right" name="sendername" value="${sendername}"/><br/>
			<label for="senderemail"><g:message code="settings.email.senderemail"/></label><g:field type="email" name="senderemail" value="${senderemail}"/><br/>
			
			<br/>
			<h2><g:message code="settings.email.account"/></h2>
			<label for="accountiban"><g:message code="settings.email.account.iban"/></label><g:textField name="accountiban" value="${accountiban}"/><br/>
			<label for="accountowner"><g:message code="settings.email.account.owner"/></label><g:textField name="accountowner" value="${accountowner}"/><br/>
			
			<br/>
			<g:submitButton name="submitButton" value="${g.message(code:'save')}" style="clear:both;"/>
		</div>
	</g:form>
	<br/>
	Automatisch versturen persoonlijke e-mails: <g:link controller="myMail" action="versturen"><g:message code="sending.mails.automatically" /></g:link><br/>
	Automatisch versturen maillijst: <g:link controller="myMail" action="maillijst"><g:message code="maillijst" /></g:link>
</body>
</html>