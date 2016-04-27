<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
<title>Gästebuch</title></head>
<body>
<h1>Das Gästebuch</h1><hr><div class="container">
<form method="POST">
<div class="form-group">
    <label for="Email">Deine Email-adresse:</label>
    <input type="email" class="form-control" id="Email" placeholder="Email" name="email"  required>
  </div>
  <div class="form-group">
    <label for="TA">Deine Botschaft</label>
    <textarea class="form-control" rows="3" id="TA" name="M" required></textarea>
  </div> bold funktioniert
<input type="hidden" name="sent"></input>
<button type="submit" class="btn btn-success">Senden</button>
</form>
<div class="well">

Bisherige Einträge:<br><%=request.getAttribute("Inhalt") %>

</div></div>


</body>

</html>