package com.everypicfound.imageasset.application.service;

import org.springframework.stereotype.Service;

import com.everypicfound.common.response.PageResult;
import com.everypicfound.imageasset.application.command.ImageAssetQueryCriteria;
import com.everypicfound.imageasset.application.command.ImageUploadCommand;
import com.everypicfound.imageasset.application.dto.ImageAssetDTO;
import com.everypicfound.imageasset.application.result.ImageUploadResult;

@Service
public class DefaultImageAssetApplicationService implements ImageAssetApplicationService {
 
    @Override
    public ImageUploadResult upload(ImageUploadCommand command) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ImageAssetDTO getDetail(Long imageId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PageResult<ImageAssetDTO> pageQuery(ImageAssetQueryCriteria criteria) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(Long imageId) {
        throw new UnsupportedOperationException("TODO");
    }
}
