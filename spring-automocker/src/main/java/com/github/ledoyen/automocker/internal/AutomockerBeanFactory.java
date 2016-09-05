package com.github.ledoyen.automocker.internal;

import java.beans.PropertyEditor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.StringUtils;

import com.github.ledoyen.automocker.BeanDefinitionModifier;
import com.github.ledoyen.automocker.configuration.AutomockerConfiguration;

public class AutomockerBeanFactory extends DefaultListableBeanFactory {

	private final AutomockerConfiguration configuration;
	private ConfigurableApplicationContext applicationContext;

	private boolean freezeStarted = false;

	private final Set<BeanDefinitionModifier> matchedModifiers = new HashSet<>();

	public AutomockerBeanFactory(AutomockerConfiguration configuration, ConfigurableApplicationContext context) {
		this.configuration = configuration;
		this.applicationContext = context;
	}

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		if (!freezeStarted) {
			Optional.ofNullable(BeanDefinitions.extractClass(beanDefinition)).flatMap(definitionClass -> configuration.getModifier(definitionClass)).ifPresent(modifier -> {
				matchedModifiers.add(modifier._2());
				modifier._2().modify(modifier._1(), beanName, (AbstractBeanDefinition) beanDefinition, (name, def) -> super.registerBeanDefinition(name, def));
			});
		}
		super.registerBeanDefinition(beanName, beanDefinition);
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
		super.registerSingleton(beanName, singletonObject);
	}

	@Override
	public void freezeConfiguration() {
		freezeStarted = true;
		matchedModifiers.forEach(modifier -> modifier.afterModifications(this));

		super.freezeConfiguration();
	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		// TODO change by configuration and @ReplaceBeanPostProcessor
		try {
			Class<?> clazz = Class.forName("org.springframework.context.support.ApplicationContextAwareProcessor");
			if (clazz.isAssignableFrom(beanPostProcessor.getClass())) {
				super.addBeanPostProcessor(new AutomockerApplicationContextAwareProcessor(applicationContext));
			} else {
				super.addBeanPostProcessor(beanPostProcessor);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static Pattern placeholderRegexp = Pattern.compile("(\\$.*\\{)(.*)\\}");

	@Override
	public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
		if (ResourceEditorRegistrar.class.isInstance(registrar)) {
			super.addPropertyEditorRegistrar(new ResourceEditorRegistrar(applicationContext, applicationContext.getEnvironment()) {
				public void registerCustomEditors(PropertyEditorRegistry registry) {
					super.registerCustomEditors(registry);
					ResourceEditor baseEditor = new ResourceEditor(applicationContext, applicationContext.getEnvironment()) {
						@Override
						public void setAsText(String text) {
							if (StringUtils.hasText(text)) {
								Matcher matcher = placeholderRegexp.matcher(text);

								if (matcher.matches()) {
									Optional<String> content = resolveContent(applicationContext.getEnvironment(), matcher.group(1), matcher.group(2));
									if (content.isPresent()) {
										setValue(new ByteArrayResource(content.get().getBytes(), "Automocker inlined resource"));
									} else {
										super.setAsText(text);
									}
								} else {
									super.setAsText(text);
								}
							} else {
								setValue(null);
							}
						}
					};
					doRegisterEditor(registry, Resource.class, baseEditor);
				}

				private void doRegisterEditor(PropertyEditorRegistry registry, Class<?> requiredType, PropertyEditor editor) {
					if (registry instanceof PropertyEditorRegistrySupport) {
						((PropertyEditorRegistrySupport) registry).overrideDefaultEditor(requiredType, editor);
					} else {
						registry.registerCustomEditor(requiredType, editor);
					}
				}
			});
		} else {
			super.addPropertyEditorRegistrar(registrar);
		}
	}

	private Optional<String> resolveContent(PropertyResolver propertyResolver, String prefix, String placeholder) {
		try {
			String content = applicationContext.getEnvironment().resolveRequiredPlaceholders(prefix + placeholder + ".content" + "}");
			return Optional.of(content);
		} catch (IllegalArgumentException e) {
			// eat here, nothing to do
		}
		return Optional.empty();
	}
}
