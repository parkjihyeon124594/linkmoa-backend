package com.knu.linkmoa.domain.site.entity;


import com.knu.linkmoa.domain.directory.entity.Directory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name="sites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="site_id")
    private Long id;

    @Column(name="site_name")
    private String siteName;

    @Column(name="site_url")
    private String siteUrl;

    @ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="directory_id")
    private Directory directory;

    @Column(name="member_id")
    private Long memberId;


    @Builder
    public Site(String siteName,String siteUrl,Directory directory,Long memberId){
        this.siteName=siteName;
        this.siteUrl=siteUrl;
        this.memberId=memberId;
        setDirectory(directory);
    }

    public void setDirectory(Directory directory){
        this.directory = directory;
        directory.getSites().add(this);
    }

    public void updateSite(String siteName,String siteUrl){
        this.siteName=siteName;
        this.siteUrl=siteUrl;
    }
}
