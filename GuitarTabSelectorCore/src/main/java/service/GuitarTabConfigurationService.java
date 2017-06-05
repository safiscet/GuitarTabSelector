package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidConfigurationException;
import model.GuitarTabConfiguration;
import org.apache.commons.lang3.StringUtils;
import util.FormatUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class GuitarTabConfigurationService {

    public GuitarTabConfiguration getFromJsonFile(String pathToJsonFile) throws InvalidConfigurationException {
        File jsonFile = new File(pathToJsonFile);
        return getFromJsonFile(jsonFile);
    }

    public GuitarTabConfiguration getFromJsonFile(File jsonFile) throws InvalidConfigurationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            GuitarTabConfiguration config = mapper.readValue(jsonFile, GuitarTabConfiguration.class);
            if (StringUtils.isEmpty(config.getRootPath())) {
                throw new InvalidConfigurationException("There must be a root path!");
            }
            if (config.getFormatRanking().isEmpty()) {
                config.setFormatRanking(FormatUtils.getDefaultFormats());
            }
            return config;
        } catch (IOException e) {
            throw new InvalidConfigurationException("The file " + jsonFile + " could not be opened!", e);
        }
    }
}
