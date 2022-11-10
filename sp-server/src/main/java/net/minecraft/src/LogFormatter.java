package net.minecraft.src;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class LogFormatter extends Formatter {
	private SimpleDateFormat field_98228_b;

	final LogAgent field_98229_a;

	private LogFormatter(LogAgent par1LogAgent) {
		this.field_98229_a = par1LogAgent;
		this.field_98228_b = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public String format(LogRecord par1LogRecord) {
		StringBuilder var2 = new StringBuilder();
		var2.append(this.field_98228_b.format(Long.valueOf(par1LogRecord.getMillis())));

		if (LogAgent.func_98237_a(this.field_98229_a) != null) {
			var2.append(LogAgent.func_98237_a(this.field_98229_a));
		}

		var2.append(" [").append(par1LogRecord.getLevel().getName()).append("] ");
		var2.append(this.formatMessage(par1LogRecord));
		var2.append('\n');
		Throwable var3 = par1LogRecord.getThrown();

		if (var3 != null) {
			StringWriter var4 = new StringWriter();
			var3.printStackTrace(new PrintWriter(var4));
			var2.append(var4.toString());
		}

		return var2.toString();
	}

	LogFormatter(LogAgent par1LogAgent, LogAgentINNER1 par2LogAgentINNER1) {
		this(par1LogAgent);
	}
}
