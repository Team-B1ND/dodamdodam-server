package b1nd.dodam.domain.rds.group.enumeration;

public enum GroupPermission {
    READER, WRITER, ADMIN;

    static public Boolean isAdmin(GroupPermission permission) {
        return permission == GroupPermission.ADMIN;
    }
}
