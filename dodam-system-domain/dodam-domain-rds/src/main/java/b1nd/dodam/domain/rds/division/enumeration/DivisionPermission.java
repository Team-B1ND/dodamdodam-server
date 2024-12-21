package b1nd.dodam.domain.rds.division.enumeration;

public enum DivisionPermission {
    READER, WRITER, ADMIN;

    static public Boolean isAdmin(DivisionPermission permission) {
        return permission == DivisionPermission.ADMIN;
    }
}
