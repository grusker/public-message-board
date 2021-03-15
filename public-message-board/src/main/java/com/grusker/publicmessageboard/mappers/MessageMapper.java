package com.grusker.publicmessageboard.mappers;

import com.grusker.publicmessageboard.dtos.MessageInputDto;
import com.grusker.publicmessageboard.dtos.MessageOutputDto;
import com.grusker.publicmessageboard.entities.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "createUserName", source = "loginUserName")
    MessageEntity toMessageEntity(MessageInputDto messageInputDto, String loginUserName);

    @Mapping(target = "createUserName", source = "loginUserName")
    MessageEntity toMessageEntity(Long id, MessageInputDto messageInputDto, String loginUserName);

    MessageOutputDto toMessageOutputDto(MessageEntity messageEntity);

    List<MessageOutputDto> toMessageOutputDtos(List<MessageEntity> messageEntities);
}
