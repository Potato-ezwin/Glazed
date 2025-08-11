package chorus0;

import cc.polymorphism.annot.IncludeReference;
import cc.polymorphism.eventbus.EventBus;
import com.chorus.api.command.CommandManager;
import com.chorus.api.module.BaseModule;
import com.chorus.api.module.ModuleManager;
import com.chorus.api.module.setting.SettingManager;
import com.chorus.api.repository.bot.BotRepository;
import com.chorus.api.repository.friend.FriendRepository;
import com.chorus.api.repository.team.TeamRepository;

import com.chorus.api.system.notification.NotificationManager;

import com.chorus.api.system.prot.MathProt;
import com.chorus.api.system.render.font.Fonts;
import com.chorus.api.system.rotation.RotationComponent;
import com.chorus.core.client.ClientInfo;
import com.chorus.core.client.ConcreteClientInfoProvider;

import com.chorus.core.listener.ListenerRepository;
import com.chorus.impl.modules.client.*;
import com.chorus.impl.modules.combat.*;
import com.chorus.impl.modules.movement.*;
import com.chorus.impl.modules.other.*;
import com.chorus.impl.modules.utility.*;
import com.chorus.impl.modules.visual.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;


import java.util.Comparator;


@IncludeReference
@Slf4j
public final class Chorus implements ModInitializer {
    @Getter
    private static Chorus instance;

    @Getter
    private ClientInfo clientInfo;
    @Getter
    private EventBus eventManager;
    @Getter
    private ModuleManager moduleManager;
    @Getter
    private SettingManager settingManager;
    @Getter
    private Fonts fonts;
    @Getter
    private FriendRepository friendRepository;
    @Getter
    private BotRepository npcRepository;
    @Getter
    private TeamRepository teamRepository;
    @Getter
    private CommandManager commandManager;

    @Getter
    private ListenerRepository listenerRepository;
    @Getter
    private RotationComponent rotationComponent;
    @Getter
    private NotificationManager NotificationManager;

    @Override
    public void onInitialize() {
        instance = this;
        log.info("Initializing Chorus...");
        clientInfo = new ConcreteClientInfoProvider().provideClientInfo();
        log.info("Client info loaded: {} v{}", clientInfo.name(), clientInfo.version());
        fonts = new Fonts();

        // Initialize all managers immediately without authentication
        log.info("Initializing managers...");
        eventManager = new EventBus();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        settingManager = new SettingManager();

        friendRepository = new FriendRepository();
        npcRepository = new BotRepository();
        teamRepository = new TeamRepository();
        listenerRepository = new ListenerRepository();
        rotationComponent = new RotationComponent();
        NotificationManager = new NotificationManager();
        log.info("Managers initialized successfully");

        // Register all modules
        java.util.List<Class<? extends BaseModule>> moduleClasses = java.util.Arrays.asList(
                ClickGUI.class,
                AimAssist.class,
                Chams.class,
                TriggerBot.class,
                //LagRange.class,
                AutoTotem.class,
                AutoCrystal.class,
                Teams.class,
                Friends.class,
                AntiBot.class,
                Sprint.class,
                AntiDebuff.class,
                Atmosphere.class,
                //Blink.class,
                HitSelection.class,
                SprintReset.class,
                AntiResourcePack.class,
                FastPlace.class,
                BridgeAssist.class,
                Arraylist.class,
                Speed.class,
                Watermark.class,
                ElytraSwap.class,
                AutoStun.class,
                MultiTask.class,
                Velocity.class,
                PlayerESP.class,
                Backtrack.class,
                SnapTap.class,
                HitCrystal.class,
                AnchorMacro.class,
                TargetStrafe.class,
                Nametags.class,
                ChestStealer.class,
                FlagDetector.class,
                MoveFix.class,
                AimCrosshair.class,
                StorageESP.class,
                BedPlates.class,
                Timer.class,
                NoDelay.class,
                AutoTool.class,
                Target.class,
                TargetHud.class,
                Tracers.class,
                Keybinds.class,
                GuiMove.class,
                AutoArmor.class,
                Hitboxes.class,
                //TPSSync.class,
                InventoryManager.class,
                Scaffold.class,
                NoPush.class,
                Streamer.class,
                NoRotate.class,
                //ChatBypass.class,
                Notifications.class,
                //AutoElytra.class,
                //AutoWeb.class,
                MaceSwap.class,
                FakePlayer.class,
                Piercing.class,
                //MaceKiller.class,
                Insults.class,
                Scoreboard.class,
                Radar.class,
                //Widgets.class,
                Trajectories.class,
                WaterSpeed.class,
                //AutoPlace.class,
                //AntiWeb.class,
                //HorseJump.class,
                NoToast.class,
                //TransactionConfirmBlinker.class,
                ElytraPredict.class,
                TotemAnimation.class,
                Safety.class,
                Capes.class,
                AspectRatio.class,
                ItemTransforms.class,
                Criticals.class,
                //WebSpeed.class,
                //KillAura.class,
                SelfDestruct.class,
                TickBase.class,
                Prevent.class
                //CrystalAura.class
        );

        moduleClasses.stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .forEach(moduleClass -> {
                    try {
                        moduleManager.registerModule(moduleClass.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        log.error("Failed to register module: {}", moduleClass.getSimpleName(), e);
                    }
                });

        listenerRepository.setup();
        commandManager.init();
        MathProt.initializeConstants();
        log.info("Chorus initialized successfully!");
    }
}
