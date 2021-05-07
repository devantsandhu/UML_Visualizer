package src.ast;

public enum Visibility {
    isPublic("public"),
    isPrivate("private"),
    isProtected("protected"),
    isDefault(""),
    fieldNotNeeded(""); //In case of Enum types

    private String keyword;

    Visibility(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
