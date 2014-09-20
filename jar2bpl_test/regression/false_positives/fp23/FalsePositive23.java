

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Destroys all registered <code>Process</code>es when the VM exits.
 * In file: ProcessDestroyer.java
	line 86
 * @since Ant 1.5
 */
class FalsePositive23 implements Runnable {
	private static final int THREAD_DIE_TIMEOUT = 20000;
	private HashSet processes = new HashSet();
	// methods to register and unregister shutdown hooks
	private Method addShutdownHookMethod;
	private Method removeShutdownHookMethod;
	private ProcessDestroyerImpl destroyProcessThread = null;

	// whether or not this ProcessDestroyer has been registered as a
	// shutdown hook
	private boolean added = false;
	// whether or not this ProcessDestroyer is currently running as
	// shutdown hook
	private boolean running = false;

	private class ProcessDestroyerImpl extends Thread {
		private boolean shouldDestroy = true;

		public ProcessDestroyerImpl() {
			super("ProcessDestroyer Shutdown Hook");
		}

		public void run() {
			if (shouldDestroy) {
				FalsePositive23.this.run();
			}
		}

		public void setShouldDestroy(boolean shouldDestroy) {
			this.shouldDestroy = shouldDestroy;
		}
	}

	/**
	 * Constructs a <code>ProcessDestroyer</code> and obtains
	 * <code>Runtime.addShutdownHook()</code> and
	 * <code>Runtime.removeShutdownHook()</code> through reflection. The
	 * ProcessDestroyer manages a list of processes to be destroyed when the VM
	 * exits. If a process is added when the list is empty, this
	 * <code>ProcessDestroyer</code> is registered as a shutdown hook. If
	 * removing a process results in an empty list, the
	 * <code>ProcessDestroyer</code> is removed as a shutdown hook.
	 */
	FalsePositive23() {
		try {
			// check to see if the shutdown hook methods exists
			// (support pre-JDK 1.3 and Non-Sun VMs)
			Class[] paramTypes = { Thread.class };
			addShutdownHookMethod = Runtime.class.getMethod("addShutdownHook",
					paramTypes);

			removeShutdownHookMethod = Runtime.class.getMethod(
					"removeShutdownHook", paramTypes);
			// wait to add shutdown hook as needed
		} catch (NoSuchMethodException e) {
			// it just won't be added as a shutdown hook... :(
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers this <code>ProcessDestroyer</code> as a shutdown hook, uses
	 * reflection to ensure pre-JDK 1.3 compatibility.
	 */
	private void addShutdownHook() {
	}

	/**
	 * Removes this <code>ProcessDestroyer</code> as a shutdown hook, uses
	 * reflection to ensure pre-JDK 1.3 compatibility
	 */
	private void removeShutdownHook() {

	}

	/**
	 * Returns whether or not the ProcessDestroyer is registered as as shutdown
	 * hook
	 * 
	 * @return true if this is currently added as shutdown hook
	 */
	public boolean isAddedAsShutdownHook() {
		return added;
	}

	/**
	 * Returns <code>true</code> if the specified <code>Process</code> was
	 * successfully added to the list of processes to destroy upon VM exit.
	 * 
	 * @param process
	 *            the process to add
	 * @return <code>true</code> if the specified <code>Process</code> was
	 *         successfully added
	 */
	public boolean add(Process process) {
		synchronized (processes) {
			// if this list is empty, register the shutdown hook
			if (processes.size() == 0) {
				addShutdownHook();
			}
			return processes.add(process);
		}
	}

	/**
	 * Returns <code>true</code> if the specified <code>Process</code> was
	 * successfully removed from the list of processes to destroy upon VM exit.
	 * 
	 * @param process
	 *            the process to remove
	 * @return <code>true</code> if the specified <code>Process</code> was
	 *         successfully removed
	 */
	public boolean remove(Process process) {
		synchronized (processes) {
			boolean processRemoved = processes.remove(process);
			if (processRemoved && processes.size() == 0) {
				removeShutdownHook();
			}
			return processRemoved;
		}
	}

	/**
	 * Invoked by the VM when it is exiting.
	 */
	public void run() {
		synchronized (processes) {
			running = true;
			Iterator e = processes.iterator();
			while (e.hasNext()) {
				((Process) e.next()).destroy();
			}
		}
	}
}
