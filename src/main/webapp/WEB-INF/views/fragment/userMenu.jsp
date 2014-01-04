<ul class="nav pull-right">
    <li class="dropdown">
        <a
            id="drop1"
            href="#"
            role="button"
            class="dropdown-toggle"
            data-toggle="dropdown">Profile<b class="caret"></b></a>
        <ul
            class="dropdown-menu"
            role="menu"
            aria-labelledby="drop1">
            <li role="presentation">
                <a
                    role="menuitem"
                    tabindex="-1"
                    href="<c:url value="/webapp/profile/change-password" />">
                    <i class="icon-pencil"></i> Change password
                </a>
            </li>
            <li role="presentation">
                <a
                    role="menuitem"
                    tabindex="-1"
                    href="<c:url value="/webapp/security/logout" />"><i
                    class="icon-off"></i>Logout</a>
            </li>
        </ul>
    </li>
</ul>