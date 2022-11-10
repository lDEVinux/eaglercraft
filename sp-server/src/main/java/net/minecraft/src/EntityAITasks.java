package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityAITasks {
	/** A list of EntityAITaskEntrys in EntityAITasks. */
	private List taskEntries = new ArrayList();

	/** A list of EntityAITaskEntrys that are currently being executed. */
	private List executingTaskEntries = new ArrayList();

	/** Instance of Profiler. */
	private final Profiler theProfiler;
	private int field_75778_d = 0;
	private int field_75779_e = 3;

	public EntityAITasks(Profiler par1Profiler) {
		this.theProfiler = par1Profiler;
	}

	public void addTask(int par1, EntityAIBase par2EntityAIBase) {
		this.taskEntries.add(new EntityAITaskEntry(this, par1, par2EntityAIBase));
	}

	/**
	 * removes the indicated task from the entity's AI tasks.
	 */
	public void removeTask(EntityAIBase par1EntityAIBase) {
		Iterator var2 = this.taskEntries.iterator();

		while (var2.hasNext()) {
			EntityAITaskEntry var3 = (EntityAITaskEntry) var2.next();
			EntityAIBase var4 = var3.action;

			if (var4 == par1EntityAIBase) {
				if (this.executingTaskEntries.contains(var3)) {
					var4.resetTask();
					this.executingTaskEntries.remove(var3);
				}

				var2.remove();
			}
		}
	}

	public void onUpdateTasks() {
		ArrayList var1 = new ArrayList();
		Iterator var2;
		EntityAITaskEntry var3;

		if (this.field_75778_d++ % this.field_75779_e == 0) {
			var2 = this.taskEntries.iterator();

			while (var2.hasNext()) {
				var3 = (EntityAITaskEntry) var2.next();
				boolean var4 = this.executingTaskEntries.contains(var3);

				if (var4) {
					if (this.canUse(var3) && this.canContinue(var3)) {
						continue;
					}

					var3.action.resetTask();
					this.executingTaskEntries.remove(var3);
				}

				if (this.canUse(var3) && var3.action.shouldExecute()) {
					var1.add(var3);
					this.executingTaskEntries.add(var3);
				}
			}
		} else {
			var2 = this.executingTaskEntries.iterator();

			while (var2.hasNext()) {
				var3 = (EntityAITaskEntry) var2.next();

				if (!var3.action.continueExecuting()) {
					var3.action.resetTask();
					var2.remove();
				}
			}
		}

		this.theProfiler.startSection("goalStart");
		var2 = var1.iterator();

		while (var2.hasNext()) {
			var3 = (EntityAITaskEntry) var2.next();
			this.theProfiler.startSection(var3.action.getClass().getSimpleName());
			var3.action.startExecuting();
			this.theProfiler.endSection();
		}

		this.theProfiler.endSection();
		this.theProfiler.startSection("goalTick");
		var2 = this.executingTaskEntries.iterator();

		while (var2.hasNext()) {
			var3 = (EntityAITaskEntry) var2.next();
			var3.action.updateTask();
		}

		this.theProfiler.endSection();
	}

	/**
	 * Determine if a specific AI Task should continue being executed.
	 */
	private boolean canContinue(EntityAITaskEntry par1EntityAITaskEntry) {
		this.theProfiler.startSection("canContinue");
		boolean var2 = par1EntityAITaskEntry.action.continueExecuting();
		this.theProfiler.endSection();
		return var2;
	}

	/**
	 * Determine if a specific AI Task can be executed, which means that all running
	 * higher (= lower int value) priority tasks are compatible with it or all lower
	 * priority tasks can be interrupted.
	 */
	private boolean canUse(EntityAITaskEntry par1EntityAITaskEntry) {
		this.theProfiler.startSection("canUse");
		Iterator var2 = this.taskEntries.iterator();

		while (var2.hasNext()) {
			EntityAITaskEntry var3 = (EntityAITaskEntry) var2.next();

			if (var3 != par1EntityAITaskEntry) {
				if (par1EntityAITaskEntry.priority >= var3.priority) {
					if (this.executingTaskEntries.contains(var3)
							&& !this.areTasksCompatible(par1EntityAITaskEntry, var3)) {
						this.theProfiler.endSection();
						return false;
					}
				} else if (this.executingTaskEntries.contains(var3) && !var3.action.isContinuous()) {
					this.theProfiler.endSection();
					return false;
				}
			}
		}

		this.theProfiler.endSection();
		return true;
	}

	/**
	 * Returns whether two EntityAITaskEntries can be executed concurrently
	 */
	private boolean areTasksCompatible(EntityAITaskEntry par1EntityAITaskEntry,
			EntityAITaskEntry par2EntityAITaskEntry) {
		return (par1EntityAITaskEntry.action.getMutexBits() & par2EntityAITaskEntry.action.getMutexBits()) == 0;
	}
}
