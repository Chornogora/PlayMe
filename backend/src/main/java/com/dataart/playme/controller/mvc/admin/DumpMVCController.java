package com.dataart.playme.controller.mvc.admin;

import com.dataart.playme.exception.ApplicationRuntimeException;
import com.dataart.playme.service.DumpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/admin/dump")
public class DumpMVCController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpMVCController.class);

    private static final String DB_DUMP_FILEPATH = "/admin/dump.html";

    private final DumpService dumpService;

    @Autowired
    public DumpMVCController(DumpService dumpService) {
        this.dumpService = dumpService;
    }

    @GetMapping("/page")
    public String getDumpPage() {
        return DB_DUMP_FILEPATH;
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> getDump() {
        try {
            LOGGER.info("Generating dump file");
            byte[] bytes = dumpService.getDump();
            ByteArrayResource resource = new ByteArrayResource(bytes);
            return ResponseEntity.ok()
                    .contentLength(bytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }catch (IOException e) {
            throw new ApplicationRuntimeException("Error occured while creating dump", e);
        }
    }
}
