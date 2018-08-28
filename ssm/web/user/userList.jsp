<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>用户列表</title>
    <link rel="stylesheet" type="text/css" href="../css/my.css">
</head>
<body>
    <table id="t1" border="1px" class="t-1">
        <tr>
            <td>ID</td>
            <td>NAME</td>
            <td>AGE</td>
            <td>删除</td>
            <td>修改</td>
        </tr>
        <c:forEach items="${allUsers}" var="u">
            <tr>
                <td><c:out value="${u.id}"/></td>
                <td><c:out value="${u.name}"/></td>
                <td><c:out value="${u.age}"/></td>
                <td><a href='<c:url value="/user/deleteUser?uid=${u.id}"/>'>删除</a></td>
                <td><a href='<c:url value="/user/editUser?uid=${u.id}"/>'>修改</a></td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="5" style="text-align: right">
                <c:forEach begin="1" end="${pages}" step="1" var="i">
                    <c:if test="${i == param.pn}">
                        [<c:out value="${i}"/>]&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${i != param.pn}">
                        <a href='<c:url value="/user/findPage?pn=${i}" />'><c:out value="${i}"/></a>&nbsp;&nbsp;
                    </c:if>

                </c:forEach>
            </td>
        </tr>
    </table>
</body>
</html>