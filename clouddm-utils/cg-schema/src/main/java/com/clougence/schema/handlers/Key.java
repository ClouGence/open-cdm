package com.clougence.schema.handlers;

import com.clougence.utils.StringUtils;

public final class Key {

    private final String[] names;

    public Key(String... names){
        this.names = names == null ? new String[0] : names;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.names, ".");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Key key = (Key) o;

        if (key.names.length != this.names.length) {
            return false;
        }

        for (int i = 0; i < names.length; i++) {
            if (!StringUtils.equals(names[i], key.names[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (String n : names) {
            result = 31 * result + (n != null ? n.hashCode() : 0);
        }
        return result;
    }
}
