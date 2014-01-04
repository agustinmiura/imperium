<!-- Only show the menu to the admin users -->
<c:if test="${sessionScope.USER['isAdmin']}">
    <ul class="nav pull-left">
        <li class="dropdown">
            <a
                id="drop1"
                href="#"
                role="button"
                class="dropdown-toggle"
                data-toggle="dropdown">Admin<b class="caret"></b></a>
            <ul
                class="dropdown-menu"
                role="menu"
                aria-labelledby="drop1">
                <li role="presentation">
                    <a
                        role="menuitem"
                        tabindex="-1"
                        href="<c:url value="/webapp/user/list" />"> <i
                        class="icon-list"></i>User list <c:out
                            value="${sessionScope.user}" />
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</c:if>
