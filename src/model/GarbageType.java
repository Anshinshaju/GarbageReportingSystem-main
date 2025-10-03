package model;

public class GarbageType {
    private int typeId;
    private String typeName;
    
    public GarbageType(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
    
    // Getters and setters
    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    @Override
    public String toString() {
        return typeName;
    }
}