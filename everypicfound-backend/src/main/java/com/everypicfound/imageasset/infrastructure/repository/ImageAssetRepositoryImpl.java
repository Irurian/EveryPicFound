package com.everypicfound.imageasset.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.everypicfound.common.response.PageResult;
import com.everypicfound.imageasset.application.command.ImageAssetQueryCriteria;
import com.everypicfound.imageasset.application.command.ImageAssetSaveCommand;
import com.everypicfound.imageasset.application.command.ImageStatusUpdateCommand;
import com.everypicfound.imageasset.application.command.VectorStatusUpdateCommand;
import com.everypicfound.imageasset.application.dto.ImageAssetDTO;
import com.everypicfound.imageasset.domain.repository.ImageAssetRepository;

@Repository
public class ImageAssetRepositoryImpl implements ImageAssetRepository{
    @Override
    public boolean save(ImageAssetSaveCommand command) {
        throw new UnsupportedOperationException("TODO");
    }


    @Override
    public ImageAssetDTO findById(Long imageId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<ImageAssetDTO> findByIds(List<Long> imageIds) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean existsByFileHash(String fileHash) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PageResult<ImageAssetDTO> pageQuery(ImageAssetQueryCriteria criteria) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean updateImageStatus(ImageStatusUpdateCommand command) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean updateVectorStatus(VectorStatusUpdateCommand command) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean updateVectorReady(VectorStatusUpdateCommand command) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean updateVectorFailed(VectorStatusUpdateCommand command) {
        throw new UnsupportedOperationException("TODO");
    }
}
