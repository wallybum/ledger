.PHONY: user-service-test

ustest:
	./gradlew :ledger-system:user-service:test --console=plain --rerun-tasks
