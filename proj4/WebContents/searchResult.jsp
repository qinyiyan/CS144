<%@ page import="edu.ucla.cs.cs144.SearchResult"%>
<!DOCTYPE html>
<html>
<head>
  <title>Ebay Keyword Search</title>
  <meta charset = "UTF-8">
  <link rel="stylesheet" type = "text/css" href = "css/autosuggest.css">
  <script type="text/javascript" src = "javaScript/autosuggest2.js"></script>
  <script type="text/javascript" src = "javaScript/SuggestionProvider.js"></script>
  <script type="text/javascript">
    window.onload = function() {
      var oTextbox = new AutoSuggestControl(document.getElementById("query"), new StateSuggestions());
    }
    </script>
</head>
<body>
  <div>
    <h2>Keyword Search Result</h2>
    <form action ="search">
    <input type="text" id="query" name="q" value = "queryText">
    <input type="hidden" name="numResultsToSkip" value="0">
    <input type="hidden" name="numResultsToReturn" value="20">
    <input type="submit" value="search!">
  </form>
    <table>
      <%
        SearchResult[] srArr = (SearchResult[])request.getAttribute("resultArray");
        for (int i=0; i<srArr.length;i++){
        SearchResult result = srArr[i];
      %>
      <tr>
        <td><a href="item?itemID=<%= result.getItemId() %>"><%= result.getItemId() %>   <%= result.getName() %></td>
        <td></td>
      </tr>
      <%
      }
      %>
    </table>
    <a href="search?q=<%= request.getParameter("q") %>&amp;numResultsToSkip=<%= Integer.parseInt(request.getParameter("numResultsToSkip"))==0?0: Integer.parseInt(request.getParameter("numResultsToSkip"))-20 %>&amp;numResultsToReturn=<%=request.getParameter("numResultsToReturn")%>">Prev</a>
   <a href="search?q=<%= request.getParameter("q") %>&amp;numResultsToSkip=<%= srArr.length != 20 ? Integer.parseInt(request.getParameter("numResultsToSkip")) : Integer.parseInt(request.getParameter("numResultsToSkip")) + 20 %>&amp;numResultsToReturn=<%= request.getParameter("numResultsToReturn") %>">Next</a>

   

  </div>
</body>
</html>