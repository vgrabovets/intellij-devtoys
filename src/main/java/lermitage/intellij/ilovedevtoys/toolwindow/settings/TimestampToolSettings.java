package lermitage.intellij.ilovedevtoys.toolwindow.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;

@State(
    name = "TimestampTool",
    storages = @Storage("dev_toys.xml"),
    category = SettingsCategory.PLUGINS
)
public final class TimestampToolSettings implements PersistentStateComponent<TimestampToolSettings> {
    public String TIMEZONE = ZoneId.systemDefault().toString();

    @Override
    public TimestampToolSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TimestampToolSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static TimestampToolSettings getInstance() {
        return ApplicationManager.getApplication().getService(TimestampToolSettings.class);
    }
}
