/**
 * Copyright (c) 2016 云智盛世
 * Created with ArchiveStatus.
 */
package top.gabin.shop.core.constant;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author linjiabin on  16/8/4
 */
@Embeddable
public class ArchiveStatus implements Serializable {

    @Column(name = "ARCHIVED")
    protected Character archived = 'N';

    public Character getArchived() {
        return archived;
    }

    public void setArchived(Character archived) {
        this.archived = archived;
    }
}
