import java.util.*;

public class SymbolTable {

    /**
     *  Create a new global scope object.  Initialize the scope 
     *  stack so this new global scope entry is the current scope,
     *  i.e., this global scope entry must be the top entry of the scope 
     *  stack.  Also, note that from now on, this global scope will always be 
     *  at the bottom of the stack.  In this case there is only one entry 
     *  on the scope stack.  
     */
    private Stack<ScopeEntry> stack = new Stack<ScopeEntry>();
    public SymbolTable() {
        GlobalEntry global = new GlobalEntry();
        stack.push(global);
    }

    /**  
     *  Insert a binding for parameter 'symTabEntry' in the current scope, 
     *  i.e., in the top entry of the scope stack.  
     *  No ScopeEntry is pushed onto or popped off the scope stack 
     *  during this operation.  This method only adds a binding for 
     *  'symTabEntry'.  
     */
    public boolean insertBinding(Entry symTabEntry) {

        ScopeEntry top = stack.pop();
        boolean insert = top.addBinding(symTabEntry.name(), symTabEntry);
        stack.push(top);
        return insert;

    }

    /**  
     *  Return a String representing the current scope, 
     *  i.e., call toString() on the top entry.
     */
    public String currentScope() {
        return stack.peek().toString();
    }

    /**  
     *  Lookup 'name' in each ScopeEntry on the stack until found, 
     *  starting at the top (the current scope) and ending with the 
     *  global scope (the entry at the bottom of the stack). 
     *  Return null if not found.
     *
     *  This is done to handle nested scopes, i.e., so the proper binding 
     *  in the nearest enclosing scope is returned in cases when the same 
     *  name denotes two different entities.  
     */
    public Entry lookup(String name) {
        Stack<ScopeEntry> newstack = (Stack<ScopeEntry>)stack.clone();
        while(!newstack.empty()){
            Entry found = newstack.pop().lookup(name);
            if(found != null) return found;
        } 
        return null;
    }

    /**  
     *  Look up the qualified name 'name1.name2'
     *  
     *  To do this, first look up 'name1' as in the above 'lookup' method.  
     *  If found, make sure it is an instance of ClassEntry.
     *  If not a ClassEntry (or null), return null. 
     *  If it is, then look up name2 in this ClassEntry. 
     *  
     *  Return null if 'name1.name2' is not found.
     */
    public Entry lookup(String name1, String name2) {
        if((lookup(name1) != null) && ClassEntry.class.isInstance(lookup(name1))){
            return lookup(name2);
        }
        else {
            return null;
        }
    }

    /**  
     *  Return the MethodEntry instance nearest the top of the scope stack. 
     *  Return null if no such object is found in the scope stack.
     */
    public MethodEntry enclosingMethod() {
        Stack<ScopeEntry> newstack = (Stack<ScopeEntry>)stack.clone();
        while(!newstack.empty()){
            if(MethodEntry.class.isInstance(newstack.peek())) return (MethodEntry)newstack.peek();
            else newstack.pop();
        } 
        return null;
    }

    /**  
     *  Make the parameter 'scopeEntry' the current scope, i.e., push 
     *  'scopeEntry' onto the scope stack.
     *
     *  Return true if the operation is successful, otherwise return false.
     */
    public boolean enterScope(ScopeEntry scopeEntry) {
        return (stack.push(scopeEntry) != null) ? true : false;
    }

    /**  
     *  Leave the current scope by popping the current scope entry 
     *  (the top entry) off the scope stack.
     *  Return the ScopeEntry that is being popped off the stack.
     *
     *  Return null if there is an attempt to leave the global scope, 
     *  that is, do not allow the bottom entry (the global scope) 
     *  to be popped off the stack.  Thus, in such cases, return 
     *  null and do not leave the global scope.  
     */
    public ScopeEntry leaveScope() {
        return GlobalEntry.class.isInstance(stack.peek()) ? null : (ScopeEntry)stack.pop();
    }

    /**  
     *  Create a new instance of 'BlockEntry' and make it the current scope, 
     *  i.e., push the newly created 'BlockEntry' onto the top of the 
     *  scope stack.
     *
     *  Return true if the operation is successful, otherwise return false.
     */
    public boolean enterNewBlock() {
        BlockEntry block = new BlockEntry();
        return (stack.push(block) != null) ? true : false;
    }

    /**  
     *  Return a String representation of the global scope entry 
     *  (the entry at the bottom of scope stack)
     *  i.e., call toString() on the bottom entry of the stack.
     */
    public String toString() {
        ScopeEntry global = new GlobalEntry();
        Stack<ScopeEntry> newstack = (Stack<ScopeEntry>)stack.clone();
        while(!newstack.empty()){
            if(GlobalEntry.class.isInstance(newstack.peek())) return newstack.peek().toString();
            newstack.pop();
        }
        return "";
    }
}              // End of class SymbolTable            
