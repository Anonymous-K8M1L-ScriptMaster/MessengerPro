package tn.amin.mpro2.hook;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.robv.android.xposed.XC_MethodHook;
import tn.amin.mpro2.debug.Logger;
import tn.amin.mpro2.hook.listener.HookListenerResult;
import tn.amin.mpro2.hook.state.HookState;
import tn.amin.mpro2.hook.state.HookStateTracker;
import tn.amin.mpro2.orca.OrcaGateway;

public abstract class BaseHook {
    private HookStateTracker mStateTracker = null;
    private final ArrayList<Object> mListeners = new ArrayList<>();
    private HookListenerResult<Object> mListenersReturnValue = null;
    private Set<XC_MethodHook.Unhook> mUnhooks = null;

    public BaseHook() {
    }

    abstract public HookId getId();
    abstract protected Set<XC_MethodHook.Unhook> injectInternal(OrcaGateway gateway);

    public final void inject(OrcaGateway gateway) {
        if (mStateTracker.shouldNotApply()) return;

        if (mUnhooks != null) {
            Logger.verbose("Clearing previous hooks for " + getId().name());
            mUnhooks.forEach(XC_MethodHook.Unhook::unhook);
            mUnhooks.clear();
            mUnhooks = null;
        }

        try {
            mUnhooks = injectInternal(gateway);
        } catch (Throwable t) {
            Logger.error(t);
            Logger.info("Disabling hook " + getId().name() + " due to previous error");
            mStateTracker.updateState(HookState.NOT_WORKING);
            return;
        }

        if (mStateTracker.getStateValue() < HookState.APPLIED.getValue())
            mStateTracker.updateState(HookState.APPLIED);
    }

    public final void addListener(Object listener) {
        mListeners.add(listener);
    }

    protected final void notifyListeners(Consumer<Object> function) {
        for (Object listener : mListeners) {
            function.accept(listener);
        }
    }

    @SuppressWarnings("unchecked")
    protected final <R> void notifyListenersWithResult(Function<Object, HookListenerResult<R>> function) {
        HookListenerResult<R> lastListenerResult = null;
        for (Object listener: mListeners) {
            HookListenerResult<R> result = function.apply(listener);
            if (result.isConsumed) {
                mListenersReturnValue = (HookListenerResult<Object>) result;
                return;
            }
            lastListenerResult = result;
        }

        mListenersReturnValue = (HookListenerResult<Object>) lastListenerResult;
    }

    @SuppressWarnings("unchecked")
    protected final <R> HookListenerResult<R> getListenersReturnValue() {
        return (HookListenerResult<R>) mListenersReturnValue;
    }

    public final void setStateTracker(HookStateTracker tracker) {
        mStateTracker = tracker;
    }

    public final HookStateTracker getStateTracker() {
        return mStateTracker;
    }

    public boolean requiresUI() {
        return false;
    }

    /**
     * Wraps a methodHook with a HookStateTracker, automatically updating in case of errors ({@link HookState#NOT_WORKING})
     * and success ({@link HookState#WORKING})
     * @param methodHook
     * @return wrapped methodHook
     */
    public XC_MethodHook wrap(XC_MethodHook methodHook) {
        return new TrackerMethodHook(methodHook);
    }

    /**
     * Like {@link #wrap(XC_MethodHook)}, but does not update state on success with {@link HookState#WORKING}
     * @param methodHook
     * @return wrapped methodHook
     */
    public XC_MethodHook wrapIgnoreWorking(XC_MethodHook methodHook) {
        return new TrackerMethodHook(methodHook, true);
    }

    public <O> Predicate<O> wrap(Predicate<O> predicate) {
        return new TrackerPredicate<>(predicate);
    }

    public class TrackerMethodHook extends XC_MethodHook {

        private final XC_MethodHook mMethodHook;
        private final boolean mIgnoreWorking;

        public TrackerMethodHook(XC_MethodHook methodHook, boolean ignoreWorking) {
            mMethodHook = methodHook;
            mIgnoreWorking = ignoreWorking;
        }

        public TrackerMethodHook(XC_MethodHook methodHook) {
            this(methodHook, false);
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (mStateTracker.shouldNotApply()) return;

            try {
                Method beforeHookedMethod = mMethodHook.getClass().getDeclaredMethod("beforeHookedMethod", MethodHookParam.class);
                beforeHookedMethod.setAccessible(true);
                beforeHookedMethod.invoke(mMethodHook, param);

                if (!mIgnoreWorking) {
                    mStateTracker.updateState(HookState.WORKING);
                }
            } catch (NoSuchMethodException ignored) {
            } catch (Throwable t) {
                Logger.error(t);
                mStateTracker.updateState(HookState.NOT_WORKING);
            }
        }
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (mStateTracker.shouldNotApply()) return;

            try {
                Method afterHookedMethod = mMethodHook.getClass().getDeclaredMethod("afterHookedMethod", MethodHookParam.class);
                afterHookedMethod.setAccessible(true);
                afterHookedMethod.invoke(mMethodHook, param);

                if (!mIgnoreWorking) {
                    mStateTracker.updateState(HookState.WORKING);
                }
            } catch (NoSuchMethodException ignored) {
            } catch (Throwable t) {
                Logger.error(t);
                mStateTracker.updateState(HookState.NOT_WORKING);
            }
        }
    }

    public class TrackerPredicate<O> implements Predicate<O> {
        private final Predicate<O> mPredicate;

        public TrackerPredicate(Predicate<O> predicate) {
            mPredicate = predicate;
        }

        @Override
        public boolean test(O object) {
            if (mStateTracker.shouldNotApply()) return false;

            try {
                boolean result = mPredicate.test(object);

                mStateTracker.updateState(HookState.WORKING);

                return result;
            } catch (Throwable t) {
                Logger.error(t);
                mStateTracker.updateState(HookState.NOT_WORKING);
            }

            return false;
        }
    }
}
