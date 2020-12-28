package tip.utils.variables.defaults;


/**
 * @author 感谢2184788258 提供的demo
 * Create on 2020/12/28 19:09
 * Package tip.utils.variables.utils
 */
public class Variable {
    protected String name;
    public Variable(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Variable){
            return ((Variable) obj).name.equalsIgnoreCase(name);
        }
        return false;
    }

    public String getName(){
        return this.name;
    }

    public String value(Object args){
        return args.getClass().getName();
    }
}
