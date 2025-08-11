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

    @Value("${upload.path.image}")
    private String imagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/notice/**")
                .addResourceLocations("file:" + noticePath + "/");

        registry.addResourceHandler("/upload/inquiries/**")
                .addResourceLocations("file:" + inquiriesPath + "/");

        registry.addResourceHandler("/upload/banner/**")
                .addResourceLocations("file:" + bannerPath + "/");

        registry.addResourceHandler("/upload/image/**")  // 이미지 리소스 핸들러
                .addResourceLocations("file:" + imagePath + "/");
    }
}
