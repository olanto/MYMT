<%-- 
    Document   : index
    Created on : 3 sept. 2013, 12:27:38
    Author     : simple
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Olanto Translate service</h1>
        try http://srv2.olanto.org/R1/translate/query?key=CORPONU&source=fr&target=en&q=bonjour
        <h2>Mandatory Parameters</h2>
        <p>source value in {en,fr}</p>
        <p>target value in {en,fr}</p>
        <p>q value: sentence to be translated</p>
        <h2>Optionnal Parameters</h2>
         <p>key value in {CORPONU}</p>
         <p>debug value in {yes,no}</p>
         <h2>Result json</h2>
          <p>Result is ok</p>
         <pre>
{"data":
  {"isError":"false",
   "message":"",
   "translatedText":"bonjour"
   }
}
         </pre>
         <p>Result with error</p>
         <pre>
{"data":
  {"isError":"true",
   "message":"Error during translation, check parameters and service availabity please",
   "translatedText":"Error during translation for this : hello from en to fr"
   }
}
         </pre>
    </body>
</html>
