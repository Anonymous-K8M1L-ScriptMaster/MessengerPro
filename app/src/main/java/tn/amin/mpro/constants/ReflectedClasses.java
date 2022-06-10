package tn.amin.mpro.constants;

import tn.amin.mpro.utils.XposedHilfer;

public class ReflectedClasses {
    // Android and Kotlin classes
    public Class<?> X_ListenerInfo;
    public Class<?> X_ResourcesImpl;
    public Class<?> X_RegularImmutableList;
    public Class<?> X_ImmutableList;
    // Messenger classes
    public Class<?> X_ComponentHost;
    public Class<?> X_ContainerView;
    public Class<?> X_OneLineComposerView;
    public Class<?> X_MediaResource;
    public Class<?> X_ComposeFragment;
    public Class<?> X_CommandInterface;
    public Class<?> X_Message;
    public Class<?> X_FbDraweeView;
    // Obfuscated Messenger classes (I Guessed their names)
    public Class<?> X_FancyDialogBuilderHelper;
    public Class<?> X_FancyDialogBuilder;
    public Class<?> X_FancyDialogColorApplier;
    public Class<?> X_LoadingDialog;
    public Class<?> X_UniversalDialog;
    public Class<?> X_MUtilities;
    public Class<?> X_ThreadKey;
    public Class<?> X_ButtonClicked;
    public Class<?> X_MentionsSearchAdapter;
    public Class<?> X_MediaResourceHelper;
    public Class<?> X_MediaResourceInitilizer;
    public Class<Enum> X_MediaResourceType;
    public Class<?> X_MoreDrawerGenericGridItemData;
    public Class<?> X_MoreDrawerGenericGridItemDataStore;
    public Class<?> X_MoreDrawerGenericGridItemDataStoreInit;
    public Class<Enum> X_IconType;
    public Class<Enum> X_ColorType;
    public Class<?> X_ClassInjector;
    public Class<?> X_MessageReactor;
    public Class<?> X_UserRetriever;

    private boolean mInitDone = false;
    public void init() {
        if (mInitDone) return;
        mInitDone = true;

        // View.ListenerInfo is private
        X_ListenerInfo = XposedHilfer.findClass("android.view.View$ListenerInfo");
        X_ResourcesImpl = XposedHilfer.findClass("android.content.res.ResourcesImpl");
        X_RegularImmutableList = XposedHilfer.findClass("com.google.common.collect.RegularImmutableList");
        X_ImmutableList = XposedHilfer.findClass("com.google.common.collect.ImmutableList");
        X_ContainerView = XposedHilfer.findClass("com.facebook.messaging.composer.ComposerBarEditorActionBarContainerView");
        X_ComponentHost = XposedHilfer.findClass("com.facebook.litho.ComponentHost");
        X_MediaResource = XposedHilfer.findClass("com.facebook.ui.media.attachments.model.MediaResource");
        X_FbDraweeView = XposedHilfer.findClass("com.facebook.drawee.fbpipeline.FbDraweeView");
        X_Message = XposedHilfer.findClass("com.facebook.messaging.model.messages.Message");
        X_OneLineComposerView = XposedHilfer.findClass("com.facebook.messaging.composer.OneLineComposerView");
        X_ComposeFragment = XposedHilfer.findClass("com.facebook.messaging.composer.ComposeFragment");
        X_ThreadKey = XposedHilfer.findClass("com.facebook.messaging.model.threadkey.ThreadKey");
        X_FancyDialogBuilderHelper = XposedHilfer.findClass("X.G4p");
        X_FancyDialogBuilder = XposedHilfer.findClass("X.GPo");
        X_FancyDialogColorApplier = XposedHilfer.findClass("X.2tN");
        X_LoadingDialog = XposedHilfer.findClass("X.AGw");
        X_UniversalDialog = XposedHilfer.findClass("X.3Ne");
        X_MUtilities = XposedHilfer.findClass("X.0rQ");
        X_ButtonClicked = XposedHilfer.findClass("X.1pP");
        X_CommandInterface = XposedHilfer.findClass("X.5WC");
        X_MediaResourceHelper = XposedHilfer.findClass("X.2Pd");
        X_MediaResourceInitilizer = XposedHilfer.findClass("X.48l");
        X_MediaResourceType = (Class<Enum>) XposedHilfer.findClass("X.2Qk");
        X_MentionsSearchAdapter = XposedHilfer.findClass("X.4iE");
        X_MoreDrawerGenericGridItemData = XposedHilfer.findClass("X.ARa");
        X_MoreDrawerGenericGridItemDataStore = XposedHilfer.findClass("X.2xK");
        X_MoreDrawerGenericGridItemDataStoreInit = XposedHilfer.findClass("X.2xJ");
        X_IconType = (Class<Enum>) XposedHilfer.findClass("X.1Rz");
        X_ColorType = (Class<Enum>) XposedHilfer.findClass("X.1kI");
        X_ClassInjector = XposedHilfer.findClass("X.0sK");
        X_MessageReactor = XposedHilfer.findClass("X.6H7");
        X_UserRetriever = XposedHilfer.findClass("X.0ta"); // String "viewer context id and logged in user id should always be the same in"
    }

    public void initUrgent() {
        X_UserRetriever = XposedHilfer.findClass("X.0ta");
    }
}
