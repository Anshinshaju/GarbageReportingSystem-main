package service;

import model.GarbageType;
import repository.GarbageTypeRepository;
import java.util.List;

public class GarbageTypeService {
    private GarbageTypeRepository garbageTypeRepository;
    
    public GarbageTypeService() {
        this.garbageTypeRepository = new GarbageTypeRepository();
    }
    
    public List<GarbageType> getAllGarbageTypes() {
        return garbageTypeRepository.getAllGarbageTypes();
    }
}