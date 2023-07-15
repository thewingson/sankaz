package kz.open.sankaz.migration.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sys_file")
public class SysFile {
    @Id
    private Long id;

    @Column
    private String extension;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "size")
    private Long size;

}
