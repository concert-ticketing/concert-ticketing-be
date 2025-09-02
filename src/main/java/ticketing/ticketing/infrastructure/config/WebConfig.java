package ticketing.ticketing.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path.notice}")
    private String noticePath;

    @Value("${upload.path.inquiries}")
    private String inquiriesPath;

    @Value("${upload.path.banner}")
    private String bannerPath;

    @Value("${upload.path.thumbnail}")
    private String thumbnailPath;

    @Value("${upload.path.description}")
    private String descriptionPath;

    @Value("${upload.path.svg_image}")  // 추가
    private String svgImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/notice/**")
                .addResourceLocations("file:" + noticePath + "/");

        registry.addResourceHandler("/upload/inquiries/**")
                .addResourceLocations("file:" + inquiriesPath + "/");

        registry.addResourceHandler("/upload/banner/**")
                .addResourceLocations("file:" + bannerPath + "/");

        registry.addResourceHandler("/uploads/thumbnail/**")
                .addResourceLocations("file:" + thumbnailPath + "/");

        registry.addResourceHandler("/uploads/description/**")
                .addResourceLocations("file:" + descriptionPath + "/");

        registry.addResourceHandler("/uploads/svg_image/**")
                .addResourceLocations("file:" + svgImagePath + "/");
    }
}
