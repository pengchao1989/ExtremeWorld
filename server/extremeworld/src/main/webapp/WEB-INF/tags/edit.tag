<%@tag pageEncoding="UTF-8"%>
	<form id="inputForm" action="${ctx}/task/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${task.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<div class="controls">
					<input type="text" id="task_title" name="title"   value="${task.title}" class="form-control"  placeholder="标题" minlength="3"/>
				</div>
			</div>	
			<div class="control-group">
				<div class="controls">
					<br/>
					<textarea id="editor" placeholder="这里输入内容" autofocus></textarea>
				</div>
			</div>	
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
	
	