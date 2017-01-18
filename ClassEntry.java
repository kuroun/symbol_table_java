
public class ClassEntry extends ScopeEntry {
    public ClassEntry(String name) {
        super(name);
    }

    /** 
     * Only instances of VariableEntry, MethodEntry, or ClassEntry can be 
     * inserted into a class scope.
     * If symTabEntry is a VariableEntry, MethodEntry, or ClassEntry,
     * then insert it into the local symbol table declared in the 
     * superclass (ScopeEntry), otherwise return false.  
     */
    public boolean addBinding(String name, Entry symTabEntry) {
        return BlockEntry.class.isInstance(symTabEntry) ? false : super.addBinding(name, symTabEntry);
    }

    /** 
     * Return a String representation of the class.
     */
    public String toString() {
        return "class " + super.name() + "{\n" + super.toString() + "}\n";
    }
}              // End of class ClassEntry            

