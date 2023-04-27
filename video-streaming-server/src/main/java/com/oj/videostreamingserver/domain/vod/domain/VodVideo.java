package com.oj.videostreamingserver.domain.vod.domain;

import com.oj.videostreamingserver.domain.vod.component.PathManager;
import com.oj.videostreamingserver.domain.vod.dto.VodPostRequestBody;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Table("video")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VodVideo {

    public enum Status{PUBLIC("public"),PRIVATE("private"),DRAFT("draft");
        private String keyword;

        Status(String keyword) {
            this.keyword = keyword;
        }
    };

    @Id
    @Column("video_id")
    private UUID videoId;

    @Column("root_path")
    private Path rootPath;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("created_time")
    public LocalDateTime createdTime;

    @Column("updated_time")
    public LocalDateTime updatedTime;

    @Column("view_count")
    private int viewCount;

    @Column("channel_id")
    Long channelId;

    @Column("status")
    Status status;

    private VodVideo(UUID videoId, Path rootPath, String title, String description, LocalDateTime createdTime, LocalDateTime updatedTime, int viewCount, Long channelId, Status status) {
        this.videoId = videoId;
        this.rootPath = rootPath.toAbsolutePath();
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.viewCount = viewCount;
        this.channelId = channelId;
        this.status = status;
    }



    public static VodVideo createEntity(UUID videoId, Path rootPath, String title, String description, Long channelId){
        return new VodVideo(videoId,rootPath,title,description,LocalDateTime.now(),LocalDateTime.now(),0,channelId,Status.DRAFT);
    }


    public static VodVideo createEntity(VodPostRequestBody dto, Long channelId){
        Assert.notNull(dto.getVideoFile(),"비디오 파일은 필수 값입니다.");
        Assert.notNull(channelId,"미디어 루트 경로는 필수 값입니다.");

        UUID videoId = UUID.randomUUID();
        return createEntity(videoId,
                PathManager.VodPath.rootOf(videoId),
                dto.getTitle(),
                dto.getDescribe(),
                channelId);
    }

}