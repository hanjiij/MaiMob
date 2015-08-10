package et.maimob.com.et.datatype;

import android.graphics.drawable.Drawable;

/**
 * Created by HJ on 2015/7/16.
 */
public class FunctionInfo {
    private String name;
    private int id;
    private Drawable ico;

    public FunctionInfo() {
    }

    public FunctionInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public FunctionInfo(int id, String name,Drawable ico) {
        this.id = id;
        this.name = name;
        this.ico=ico;
    }

    public Drawable getIco() {
        return ico;
    }

    public void setIco(Drawable ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionInfo that = (FunctionInfo) o;

        if (id != that.id) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id;
        return result;
    }
}
