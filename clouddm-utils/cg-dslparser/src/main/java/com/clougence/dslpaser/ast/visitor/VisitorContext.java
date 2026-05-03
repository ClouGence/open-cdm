package com.clougence.dslpaser.ast.visitor;

public class VisitorContext<T> {

    private final T               inst;
    private final VisitorChildren children;

    public VisitorContext(T inst){
        this(inst, null);
    }

    public VisitorContext(T inst, VisitorChildren children){
        this.inst = inst;
        this.children = children;
    }

    public T getInst() { return this.inst; }

    public void visitChildren(Visitor visitor) {
        if (this.children != null) {
            this.children.visitChildren(visitor);
        }
    }
}
